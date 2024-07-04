package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.internal.util.IWaitGroup;
import com.volcengine.tos.internal.util.NullWaitGroup;
import com.volcengine.tos.internal.util.WaitGroup;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RecursiveDeleter {
    private final TosObjectRequestHandler handler;
    private final DeleteObjectInput input;
    private final boolean hns;
    private final AtomicReference<DeleteMultiObjectsV2Output> result;
    private final AtomicReference<TosException> err;

    RecursiveDeleter(DeleteObjectInput input, boolean hns, TosObjectRequestHandler handler) {
        this.input = input;
        this.hns = hns;
        this.handler = handler;
        this.result = new AtomicReference<>();
        this.err = new AtomicReference<>();
    }

    DeleteObjectOutput deleteRecursive() throws TosException {
        if (!this.input.isRecursive()) {
            throw new TosClientException("tos: recursive is false", null);
        }

        DeleteObjectInput.DeleteObjectRecursiveOption option = new DeleteObjectInput.DeleteObjectRecursiveOption();
        if (this.input.getRecursiveOption() != null) {
            if (this.input.getRecursiveOption().getBatchDeleteSize() > 0 && this.input.getRecursiveOption().getBatchDeleteSize() <= 1000) {
                option.setBatchDeleteSize(this.input.getRecursiveOption().getBatchDeleteSize());
            }

            if (this.input.getRecursiveOption().getBatchDeleteTaskNum() > 0) {
                option.setBatchDeleteTaskNum(this.input.getRecursiveOption().getBatchDeleteTaskNum());
            }
            if (this.input.getRecursiveOption().getDeleteFailedRetryCount() > 0) {
                option.setDeleteFailedRetryCount(this.input.getRecursiveOption().getDeleteFailedRetryCount());
            }
            if (this.input.getRecursiveOption().getEventListener() != null) {
                option.setEventListener(this.input.getRecursiveOption().getEventListener());
            }
        }

        if (this.hns || forceUseHns(this.input.getRecursiveOption())) {
            return this.deleteRecursiveHns(option);
        }

        return this.deleteRecursiveFns(option);
    }

    boolean forceUseHns(DeleteObjectInput.DeleteObjectRecursiveOption option) {
        if (option == null) {
            return false;
        }
        try {
            Field f = option.getClass().getDeclaredField("forceUseHns");
            f.setAccessible(true);
            return (boolean) f.get(option);
        } catch (Exception e) {
            return false;
        }
    }

    DeleteObjectOutput deleteRecursiveFns(DeleteObjectInput.DeleteObjectRecursiveOption option) throws TosException {
        DeleteMultiObjectsAggregator aggregator = new DeleteMultiObjectsAggregator(option);
        Thread dispatchThread = new Thread(new ListObjectsFlatRunnable(aggregator));
        dispatchThread.start();

        Thread aggregatorThread = new Thread(aggregator);
        aggregatorThread.start();

        try {
            dispatchThread.join();
        } catch (InterruptedException e) {
            this.err.compareAndSet(null, new TosClientException("dispatch thread is interrupted", e));
        }

        try {
            aggregator.markOfferFinished();
            aggregatorThread.join();
        } catch (InterruptedException e) {
            this.err.compareAndSet(null, new TosClientException("aggregate thread is interrupted", e));
        } finally {
            aggregator.await();
        }
        return this.checkResult();
    }

    DeleteObjectOutput deleteRecursiveHns(DeleteObjectInput.DeleteObjectRecursiveOption option) throws TosException {
        DeleteMultiObjectsSplitter splitter = new DeleteMultiObjectsSplitter(option);
        Thread dispatchThread = new Thread(new ListObjectsHierarchicalRunnable(splitter));
        dispatchThread.start();

        try {
            dispatchThread.join();
        } catch (InterruptedException e) {
            this.err.compareAndSet(null, new TosClientException("dispatch thread is interrupted", e));
        }
        return this.checkResult();
    }

    DeleteObjectOutput checkResult() {
        TosException ex = this.err.get();
        if (ex != null) {
            throw ex;
        }

        // result may be null
        RequestInfo info = this.result.get() == null ? new RequestInfo("", "", 0, new HashMap<>())
                : this.result.get().getRequestInfo();
        return new DeleteObjectOutput().setRequestInfo(info);
    }

    private class ListObjectsHierarchicalRunnable implements Runnable {
        private final DeleteMultiObjectsSplitter splitter;

        private ListObjectsHierarchicalRunnable(DeleteMultiObjectsSplitter splitter) {
            this.splitter = splitter;
        }

        @Override
        public void run() {
            this.dispatchByPrefix(RecursiveDeleter.this.input.getKey(), NullWaitGroup.getInstance());
            this.splitter.await();
        }

        private void dispatchByPrefix(String prefix, IWaitGroup wg) {
            wg.addUninterruptibly();
            ListObjectsType2Input linput = new ListObjectsType2Input().setBucket(RecursiveDeleter.this.input.getBucket()).
                    setPrefix(prefix).setMaxKeys(1000).setListOnlyOnce(true).setDelimiter("/");
            ListObjectsType2Output loutput;
            WaitGroup subWg = new WaitGroup();
            try {
                while (RecursiveDeleter.this.err.get() == null) {
                    loutput = RecursiveDeleter.this.handler.listObjectsType2(linput);
                    if (loutput.getContents() != null) {
                        List<ListedObjectV2> objects = new ArrayList<>(loutput.getContents().size());
                        for (ListedObjectV2 obj : loutput.getContents()) {
                            if (obj.getKey().endsWith("/")) { // skip folder
                                continue;
                            }
                            objects.add(obj);
                        }
                        this.splitter.offer(objects, subWg);
                    }

                    if (loutput.getCommonPrefixes() != null) {
                        for (ListedCommonPrefix p : loutput.getCommonPrefixes()) {
                            this.dispatchByPrefix(p.getPrefix(), subWg);
                        }
                    }
                    if (!loutput.isTruncated()) {
                        break;
                    }
                    linput.setContinuationToken(loutput.getNextContinuationToken());
                }

                subWg.awaitUninterruptibly();
                // delete this.prefix self
                if (RecursiveDeleter.this.err.get() == null) {
                    try {
                        DeleteObjectOutput doutput = RecursiveDeleter.this.handler.deleteObject(new DeleteObjectInput().setBucket(RecursiveDeleter.this.input.getBucket())
                                .setKey(prefix));
                        DeleteMultiObjectsV2Output output = new DeleteMultiObjectsV2Output().requestInfo(doutput.getRequestInfo())
                                .deleteds(Collections.singletonList(new Deleted().setKey(prefix)));
                        this.splitter.eventListener
                                .eventChange(new DeleteObjectInput.DeleteMultiObjectsEvent(RecursiveDeleter.this.input.getBucket(), output, null));
                    } catch (TosException ex) {
                        RecursiveDeleter.this.err.compareAndSet(null, ex);
                        this.splitter.eventListener
                                .eventChange(new DeleteObjectInput.DeleteMultiObjectsEvent(RecursiveDeleter.this.input.getBucket(), null, ex));
                    }
                }
            } catch (TosException e) {
                RecursiveDeleter.this.err.compareAndSet(null, e);
            } finally {
                wg.done();
            }
        }
    }

    private class DeleteMultiObjectsSplitter {
        private final ExecutorService batchDeleteEs;
        private final int batchDeleteSize;
        private final Semaphore maxBatchDeleteTasksInQueue;
        private final int deleteFailedRetryCount;
        private final DeleteObjectInput.DeleteMultiObjectsEventListener eventListener;

        private DeleteMultiObjectsSplitter(DeleteObjectInput.DeleteObjectRecursiveOption option) {
            this.batchDeleteEs = new ThreadPoolExecutor(option.getBatchDeleteTaskNum(), option.getBatchDeleteTaskNum(),
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            this.maxBatchDeleteTasksInQueue = new Semaphore(option.getBatchDeleteTaskNum() * 10);
            this.batchDeleteSize = option.getBatchDeleteSize();
            this.deleteFailedRetryCount = option.getDeleteFailedRetryCount();
            this.eventListener = option.getEventListener();
        }

        private void offer(List<ListedObjectV2> objects, IWaitGroup waitGroup) {
            if (RecursiveDeleter.this.err.get() != null) {
                return;
            }

            if (objects == null || objects.isEmpty()) {
                return;
            }

            int start = 0;
            while (objects.size() - start > 0 && RecursiveDeleter.this.err.get() == null) {
                int end = objects.size();
                if (end - start > batchDeleteSize) {
                    end = start + batchDeleteSize;
                }
                waitGroup.addUninterruptibly();
                this.maxBatchDeleteTasksInQueue.acquireUninterruptibly();
                this.batchDeleteEs.execute(new DeleteMultiObjectsRunnable(this.trans(objects, start, end),
                        waitGroup, this.maxBatchDeleteTasksInQueue, this.deleteFailedRetryCount, this.eventListener));
                start = end;
            }
        }

        private List<ObjectTobeDeleted> trans(List<ListedObjectV2> objects, int start, int end) {
            List<ObjectTobeDeleted> result = new ArrayList<>(end - start);
            for (int i = start; i < end; i++) {
                result.add(new ObjectTobeDeleted().setKey(objects.get(i).getKey()));
            }
            return result;
        }

        private void await() {
            this.batchDeleteEs.shutdown();
            try {
                // wait all the submitted tasks finished
                this.batchDeleteEs.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                RecursiveDeleter.this.err.compareAndSet(null, new TosClientException("aggregator is interrupted", e));
            }
        }
    }

    private class ListObjectsFlatRunnable implements Runnable {
        private final DeleteMultiObjectsAggregator aggregator;

        private ListObjectsFlatRunnable(DeleteMultiObjectsAggregator aggregator) {
            this.aggregator = aggregator;
        }

        @Override
        public void run() {
            ListObjectsType2Input linput = new ListObjectsType2Input().setBucket(RecursiveDeleter.this.input.getBucket()).
                    setPrefix(RecursiveDeleter.this.input.getKey()).setMaxKeys(1000).setListOnlyOnce(true);
            ListObjectsType2Output loutput;
            try {
                while (RecursiveDeleter.this.err.get() == null) {
                    loutput = RecursiveDeleter.this.handler.listObjectsType2(linput);
                    this.aggregator.offer(loutput.getContents());
                    if (!loutput.isTruncated()) {
                        break;
                    }
                    linput.setContinuationToken(loutput.getNextContinuationToken());
                }
            } catch (InterruptedException e) {
                RecursiveDeleter.this.err.compareAndSet(null, new TosClientException("dispatch fns is interrupted", e));
            } catch (TosException e) {
                RecursiveDeleter.this.err.compareAndSet(null, e);
            }
        }
    }

    private class DeleteMultiObjectsAggregator implements Runnable {
        private final BlockingQueue<List<ListedObjectV2>> objectsQueue;
        private final Lock lock;
        private final Condition notEmpty;
        private final Condition notFull;
        private final ExecutorService batchDeleteEs;
        private final int batchDeleteSize;
        private final Semaphore maxBatchDeleteTasksInQueue;
        private final int deleteFailedRetryCount;
        private final DeleteObjectInput.DeleteMultiObjectsEventListener eventListener;
        private List<ObjectTobeDeleted> objects;
        private List<ListedObjectV2> lastObjects;
        private int lastObjectsIndex;
        private volatile boolean offerFinished = false;

        private DeleteMultiObjectsAggregator(DeleteObjectInput.DeleteObjectRecursiveOption option) {
            this.objectsQueue = new LinkedBlockingQueue<>(option.getBatchDeleteSize() * 10);
            this.lock = new ReentrantLock();
            this.notEmpty = lock.newCondition();
            this.notFull = lock.newCondition();
            this.batchDeleteEs = new ThreadPoolExecutor(option.getBatchDeleteTaskNum(), option.getBatchDeleteTaskNum(),
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            // avoid LinkedBlockingQueue of batchDeleteEs is too large
            this.maxBatchDeleteTasksInQueue = new Semaphore(option.getBatchDeleteTaskNum() * 10);
            this.batchDeleteSize = option.getBatchDeleteSize();
            this.deleteFailedRetryCount = option.getDeleteFailedRetryCount();
            this.eventListener = option.getEventListener();
        }

        private void offer(List<ListedObjectV2> objects) throws InterruptedException {
            if (objects == null || objects.isEmpty()) {
                return;
            }

            while (RecursiveDeleter.this.err.get() == null) {
                if (this.objectsQueue.offer(objects)) {
                    break;
                }
                this.lock.lock();
                try {
                    this.notFull.await();
                } finally {
                    this.lock.unlock();
                }
            }
            this.lock.lock();
            this.notEmpty.signal();
            this.lock.unlock();
        }

        @Override
        public void run() {
            try {
                boolean continueExecute = true;
                while (RecursiveDeleter.this.err.get() == null && continueExecute) {
                    continueExecute = this.executeOne();
                }

                // flush the last
                if (RecursiveDeleter.this.err.get() == null && this.objects != null && !this.objects.isEmpty()) {
                    this.submitExecuteOne();
                    this.objects = null;
                }
            } finally {
                this.lock.lock();
                this.notFull.signal();
                this.lock.unlock();
            }
        }


        private boolean executeOne() {
            if (this.objects == null) {
                this.objects = new ArrayList<>(this.batchDeleteSize);
            }

            if (this.objects.size() == this.batchDeleteSize) {
                this.submitExecuteOne();
                this.objects = null;
                return true;
            }

            if (this.lastObjects == null) {
                while (this.objectsQueue.isEmpty()) {
                    if (this.offerFinished) {
                        return false;
                    }
                    this.lock.lock();
                    this.notEmpty.awaitUninterruptibly();
                    this.lock.unlock();
                }
                this.lastObjects = this.objectsQueue.poll();
                this.lock.lock();
                this.notFull.signal();
                this.lock.unlock();
            }

            this.objects.add(new ObjectTobeDeleted().setKey(this.lastObjects.get(this.lastObjectsIndex++).getKey()));
            if (this.lastObjectsIndex == this.lastObjects.size()) {
                this.lastObjectsIndex = 0;
                this.lastObjects = null;
            }

            return true;
        }

        private void submitExecuteOne() {
            this.maxBatchDeleteTasksInQueue.acquireUninterruptibly();
            this.batchDeleteEs.execute(new DeleteMultiObjectsRunnable(this.objects, NullWaitGroup.getInstance(),
                    this.maxBatchDeleteTasksInQueue, this.deleteFailedRetryCount, this.eventListener));
        }

        private void markOfferFinished() {
            this.offerFinished = true;
            this.lock.lock();
            this.notEmpty.signal();
            this.lock.unlock();
        }

        private void await() {
            this.batchDeleteEs.shutdown();
            try {
                // wait all the submitted tasks finished
                this.batchDeleteEs.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                RecursiveDeleter.this.err.compareAndSet(null, new TosClientException("aggregator is interrupted", e));
            }
        }

    }

    private class DeleteMultiObjectsRunnable implements Runnable {
        private List<ObjectTobeDeleted> objects;
        private final IWaitGroup waitGroup;
        private final Semaphore maxBatchDeleteTasksInQueue;
        private final int deleteFailedRetryCount;
        private final DeleteObjectInput.DeleteMultiObjectsEventListener eventListener;
        private int currentRetryCount = 0;

        private DeleteMultiObjectsRunnable(List<ObjectTobeDeleted> objects, IWaitGroup waitGroup, Semaphore maxBatchDeleteTasksInQueue,
                                           int deleteFailedRetryCount, DeleteObjectInput.DeleteMultiObjectsEventListener eventListener) {
            this.objects = objects;
            this.waitGroup = waitGroup;
            this.maxBatchDeleteTasksInQueue = maxBatchDeleteTasksInQueue;
            this.deleteFailedRetryCount = deleteFailedRetryCount;
            this.eventListener = eventListener;
        }

        @Override
        public void run() {
            this.maxBatchDeleteTasksInQueue.release();
            try {
                boolean needRetry;
                do {
                    try {
                        if (RecursiveDeleter.this.err.get() != null) {
                            return;
                        }
                        DeleteMultiObjectsV2Output output = RecursiveDeleter.this.handler.deleteMultiObjects(new DeleteMultiObjectsV2Input()
                                .setObjects(this.objects).setBucket(RecursiveDeleter.this.input.getBucket()));
                        needRetry = this.checkAndEmitEvent(output, null);
                    } catch (TosException ex) {
                        needRetry = this.checkAndEmitEvent(null, ex);
                    }
                } while (needRetry);
            } finally {
                this.waitGroup.done();
            }
        }

        private boolean checkAndEmitEvent(DeleteMultiObjectsV2Output output, TosException ex) {
            this.eventListener.eventChange(new DeleteObjectInput.DeleteMultiObjectsEvent(RecursiveDeleter.this.input.getBucket(), output, ex));
            if (ex != null) {
                RecursiveDeleter.this.err.compareAndSet(null, ex);
                return false;
            }

            if (output.getErrors() != null && !output.getErrors().isEmpty()) {
                if (this.currentRetryCount++ < this.deleteFailedRetryCount) {
                    this.objects = new ArrayList<>(output.getErrors().size());
                    for (DeleteError de : output.getErrors()) {
                        if ("AccessDenied".equals(de.getCode())) {
                            RecursiveDeleter.this.err.compareAndSet(null,
                                    new TosClientException("delete multi objects finished with AccessDenied", null));
                            return false;
                        }
                        this.objects.add(new ObjectTobeDeleted().setKey(de.getKey()));
                    }
                    return true;
                }

                RecursiveDeleter.this.err.compareAndSet(null,
                        new TosClientException("delete multi objects finished with errors", null));
            }
            return false;
        }
    }
}
