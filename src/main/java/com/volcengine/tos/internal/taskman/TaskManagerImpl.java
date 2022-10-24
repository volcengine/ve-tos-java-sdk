package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.model.object.CancelHook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class TaskManagerImpl implements TaskManager {
    private ExecutorService executor;
    private List<Future<TaskOutput<?>>> futures;
    private List<TaskOutput<?>> outputs;
    private List<TosTask> tasks;
    private InternalCancelHook internalCancelHook;
    private AbortTaskHook abortTaskHook;

    public TaskManagerImpl(int workerNum, int taskNum,
                           InternalCancelHook internalCancelHook,
                           AbortTaskHook abortTaskHook) {
        executor = new ThreadPoolExecutor(workerNum, workerNum, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        futures = new ArrayList<>(taskNum);
        outputs = new ArrayList<>(taskNum);
        tasks = new ArrayList<>(taskNum);
        this.internalCancelHook = internalCancelHook;
        this.abortTaskHook = abortTaskHook;
    }

    @Override
    public void dispatch(TosTask task) {
        this.tasks.add(task);
    }

    @Override
    public void handle() {
        for(TosTask tosTask : tasks) {
            futures.add(executor.submit(tosTask.getCallableTask()));
        }
        try{
            for (Future<TaskOutput<?>> future : futures) {
                outputs.add(future.get());
            }
        } catch (CancellationException e) {
            // 执行用户被取消
            throw new TosClientException("tos: execute canceled", e);
        } catch (InterruptedException e) {
            // 执行中断
            throw new TosClientException("tos: execute interrupted", e);
        } catch (ExecutionException e) {
            // 执行失败
            if (this.abortTaskHook != null) {
                abortTaskHook.abort();
            }
            throw new TosClientException("tos: execute failed", e);
        } finally {
            executor.shutdown();
        }
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            if (this.abortTaskHook != null) {
                abortTaskHook.abort();
            }
            throw new TosClientException("tos: await executor terminated failed", e);
        }
    }

    @Override
    public List<TaskOutput<?>> get() {
        return this.outputs;
    }

    @Override
    public void suspend() {
        if (this.executor != null) {
            for (Future<TaskOutput<?>> future : futures) {
                try{
                    future.wait();
                } catch (InterruptedException e) {
                    throw new TosClientException("tos: execute interrupted while waiting", e);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        if (this.executor != null) {
            try{
                for (Future<TaskOutput<?>> future : futures) {
                    future.cancel(true);
                }
            }finally {
                executor.shutdownNow();
            }
        }
    }
}
