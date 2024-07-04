package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AsyncTimeout extends Timeout {
    private static final int TIMEOUT_WRITE_SIZE = 64 * 1024;
    private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);

    private static final int SLOT_SIZE;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final Map<Integer, Slot> SLOT_MAP;

    static {
        int tempSlotSize = Runtime.getRuntime().availableProcessors();
        if (tempSlotSize < 16) {
            tempSlotSize = 16;
        }
        SLOT_SIZE = tempSlotSize;
        SLOT_MAP = new HashMap<>(SLOT_SIZE);
        for (int i = 0; i < SLOT_SIZE; i++) {
            SLOT_MAP.put(i, new Slot());
        }
    }

    private long timeoutAt;

    private final AtomicBoolean inQueue;

    private final AtomicReference<AsyncTimeout> next;

    private final int id;

    private final Slot slot;

    public AsyncTimeout() {
        this(null);
    }

    public AsyncTimeout(Slot slot) {
        this.inQueue = new AtomicBoolean(false);
        this.id = RANDOM.nextInt(SLOT_SIZE);
        this.next = new AtomicReference<>();
        if (slot == null) {
            this.slot = SLOT_MAP.get(this.id);
        } else {
            this.slot = slot;
        }
    }

    public void enter() {
        long timeoutNanos = timeoutNanos();
        boolean hasDeadline = hasDeadline();
        if (timeoutNanos == 0L && !hasDeadline) {
            // No timeout and no deadline? Don't bother with the queue.
            return;
        }
        this.scheduleTimeout(timeoutNanos, hasDeadline);
    }

    // Returns true if the timeout occurred.
    public boolean exit() {
        return this.cancelScheduledTimeout();
    }

    private void scheduleTimeout(long timeoutNanos, boolean hasDeadline) {
        if (!this.inQueue.compareAndSet(false, true)) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        this.slot.startWatch();
        long now = System.nanoTime();
        if (timeoutNanos != 0 && hasDeadline) {
            // Compute the earliest event; either timeout or deadline. Because nanoTime can wrap
            // around, minOf() is undefined for absolute values, but meaningful for relative ones.
            this.timeoutAt = now + Math.min(timeoutNanos, this.deadlineNanoTime() - now);
        } else if (timeoutNanos != 0) {
            this.timeoutAt = now + timeoutNanos;
        } else if (hasDeadline) {
            this.timeoutAt = this.deadlineNanoTime();
        } else {
            throw new AssertionError();
        }

        AsyncTimeout head = this.slot.getHead();
//        this.slot.acquireWriteLock();
//        try {
//            // Insert the node in sorted order.
//            long remainingNanos = this.remainingNanos(now);
//            AsyncTimeout prev = head;
//            for (; ; ) {
//                AsyncTimeout next = prev.next.get();
//                if (next == null || remainingNanos < next.remainingNanos(now)) {
//                    this.next.set(next);
//                    prev.next.set(this);
//                    if (prev == head) {
//                        // Wake up the watchdog when inserting at the front.
//                        this.slot.wakeUp();
//                    }
//                    return;
//                }
//                prev = next;
//            }
//        } finally {
//            this.slot.releaseWriteLock();
//        }

        this.slot.acquireReadLock();
        boolean released = false;
        try {
            for (; ; ) {
                // Insert the node in sorted order.
                long remainingNanos = this.remainingNanos(now);
                AsyncTimeout prev = head;
                for (; ; ) {
                    AsyncTimeout next = prev.next.get();
                    if (next == null || remainingNanos < next.remainingNanos(now)) {
                        this.next.set(next);
                        if (!prev.next.compareAndSet(next, this)) {
                            // prev next changed, retry from head again
                            now = System.nanoTime();
                            this.next.set(null);
                            break;
                        }

                        if (prev == head) {
                            this.slot.releaseReadLock();
                            released = true;
                            // Wake up the watchdog when inserting at the front.
                            this.slot.acquireWriteLock();
                            try {
                                this.slot.wakeUp();
                            } finally {
                                this.slot.releaseWriteLock();
                            }
                        }
                        return;
                    }
                    prev = next;
                }
            }
        } finally {
            if (!released) {
                this.slot.releaseReadLock();
            }
        }

    }

    private boolean cancelScheduledTimeout() {
        if (!this.inQueue.compareAndSet(true, false)) {
            return false;
        }

        AsyncTimeout head = this.slot.getHead();
        this.slot.acquireWriteLock();
        try {
            AsyncTimeout prev = head;
            for (; ; ) {
                // The node wasn't found in the linked list: it must have timed out!
                if (prev == null) {
                    return true;
                }

                AsyncTimeout next = prev.next.get();
                if (next == this) {
                    // Remove the node from the linked list.
                    prev.next.set(next.next.get());
                    next.next.set(null);
                    return false;
                }
                prev = next;
            }
        } finally {
            this.slot.releaseWriteLock();
        }
    }

    private long remainingNanos(long now) {
        return this.timeoutAt - now;
    }

    // Invoked by the watchdog thread when the time between calls to enter and exit has exceeded the timeout.
    protected void timeOut() {
    }

    // Returns a new sink that delegates to sink, using this to implement timeouts.
    // This works best if timedOut is overridden to interrupt sink's current operation.
    public Sink sink(final Sink sink) {
        return new Sink() {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                AsyncTimeout.this.checkOffsetAndCount(source.size(), 0, byteCount);

                long remaining = byteCount;
                for (; remaining > 0L; ) {
                    long toWrite = 0L;
                    for (Segment s = source.head; toWrite < TIMEOUT_WRITE_SIZE; s = s.next) {
                        int segmentSize = s.limit - s.pos;
                        toWrite += segmentSize;
                        if (toWrite >= remaining) {
                            toWrite = remaining;
                            break;
                        }
                    }
                    // Emit one write. Only this section is subject to the timeout.
                    long finalToWrite = toWrite;
                    AsyncTimeout.this.withTimeout(() -> {
                        sink.write(source, finalToWrite);
                        return null;
                    });
                    remaining -= toWrite;
                }
            }

            @Override
            public void flush() throws IOException {
                AsyncTimeout.this.withTimeout(() -> {
                    sink.flush();
                    return null;
                });
            }

            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            @Override
            public void close() throws IOException {
                AsyncTimeout.this.withTimeout(() -> {
                    sink.close();
                    return null;
                });
            }

            @Override
            public String toString() {
                return "AsyncTimeout.sink(" + sink + ")";
            }
        };
    }

    // Returns a new source that delegates to source, using this to implement timeouts.
    // This works best if timedOut is overridden to interrupt source's current operation.
    public Source source(final Source source) {
        return new Source() {
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                return AsyncTimeout.this.withTimeout(() -> source.read(sink, byteCount));
            }

            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            @Override
            public void close() throws IOException {
                AsyncTimeout.this.withTimeout(() -> {
                    source.close();
                    return null;
                });
            }

            @Override
            public String toString() {
                return "AsyncTimeout.source(" + source + ")";
            }
        };
    }

    private <T> T withTimeout(Block<T> block) throws IOException {
        boolean throwOnTimeout = false;
        this.enter();
        try {
            T result = block.execute();
            throwOnTimeout = true;
            return result;
        } catch (IOException e) {
            if (!this.exit()) {
                throw e;
            }
            throw this.newTimeoutException(e);
        } finally {
            boolean timedOut = this.exit();
            if (timedOut && throwOnTimeout) {
                throw this.newTimeoutException(null);
            }
        }
    }

    private IOException newTimeoutException(IOException cause) {
        InterruptedIOException e = new InterruptedIOException("timeout");
        if (cause != null) {
            e.initCause(cause);
        }
        return e;
    }

    private void checkOffsetAndCount(long size, long offset, long byteCount) {
        if (offset < 0 || byteCount < 0 || offset > size || size - offset < byteCount) {
            throw new ArrayIndexOutOfBoundsException("size=$size offset=$offset byteCount=$byteCount");
        }
    }

    private interface Block<T> {
        T execute() throws IOException;
    }

    static class Slot {
        private final AtomicBoolean watchStarted;
        private final ReadWriteLock rwLock;
        private final Condition cond;
        private final AsyncTimeout head;
        private final boolean startWatchThread;

        Slot() {
            this(true);
        }

        Slot(boolean startWatchThread) {
            this.watchStarted = new AtomicBoolean(false);
            this.rwLock = new ReentrantReadWriteLock();
            this.cond = this.rwLock.writeLock().newCondition();
            this.head = new AsyncTimeout();
            this.startWatchThread = startWatchThread;
        }

        private AsyncTimeout getHead() {
            return this.head;
        }

        private void acquireWriteLock() {
            this.rwLock.writeLock().lock();
        }

        private void releaseWriteLock() {
            this.rwLock.writeLock().unlock();
        }

        private void acquireReadLock() {
            this.rwLock.readLock().lock();
        }

        private void releaseReadLock() {
            this.rwLock.readLock().unlock();
        }

        private void await(long time, TimeUnit unit) throws InterruptedException {
            this.cond.await(time, unit);
        }

        private void wakeUp() {
            this.cond.signal();
        }

        private void startWatch() {
            if (!this.startWatchThread) {
                return;
            }

            if (this.watchStarted.compareAndSet(false, true)) {
                Thread t = new Thread(new Watchdog(this));
                t.setDaemon(true);
                t.start();
            }
        }

        // Removes and returns the node at the head of the list, waiting for it to time out if necessary.
        // This returns head if there was no node at the head of the list when starting, and there continues to be no node after waiting IDLE_TIMEOUT_NANOS.
        // It returns null if a new node was inserted while waiting.
        // Otherwise this returns the node being waited on that has been removed.
        private AsyncTimeout awaitTimeout() throws InterruptedException {
            // Get the next eligible node.
            AsyncTimeout head = this.getHead();
            this.acquireWriteLock();
            try {
                AsyncTimeout node = head.next.get();
                // The queue is empty. Wait until either something is enqueued or the idle timeout elapses.
                if (node == null) {
                    long startNanos = System.nanoTime();
                    this.await(IDLE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                    if (head.next.get() == null && System.nanoTime() - startNanos >= IDLE_TIMEOUT_NANOS) {
                        // The idle timeout elapsed.
                        return head;
                    }
                    // The situation has changed.
                    return null;
                }

                long waitNanos = node.remainingNanos(System.nanoTime());
                // The head of the queue hasn't timed out yet. Await that.
                if (waitNanos > 0) {
                    this.await(waitNanos, TimeUnit.NANOSECONDS);
                    return null;
                }

                // The head of the queue has timed out. Remove it.
                head.next.set(node.next.get());
                node.next.set(null);
                return node;
            } finally {
                this.releaseWriteLock();
            }
        }

        int checkAndPrintSlot() {
            AsyncTimeout head = this.getHead();
            AsyncTimeout last = null;
            int count = 0;
            for (; ; ) {
                if (head.next.get() == null) {
                    break;
                }
                long timeoutAt = head.next.get().timeoutAt;
                if (last != null && last.next.get().timeoutAt > timeoutAt) {
//                    System.out.println(timeoutAt);
                    throw new RuntimeException("found invalid order");
                }
//                System.out.println(timeoutAt);
                last = head;
                head = head.next.get();
                count++;
            }
            return count;
        }

    }

    private static class Watchdog implements Runnable {
        private final Slot slot;

        private Watchdog(Slot slot) {
            this.slot = slot;
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    AsyncTimeout timedOut = this.slot.awaitTimeout();
                    // The queue is completely empty.
                    if (timedOut == this.slot.getHead()) {
                        continue;
                    }
                    // Close the timed out node, if one was found.
                    if (timedOut != null) {
                        timedOut.timeOut();
                    }
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }
}
