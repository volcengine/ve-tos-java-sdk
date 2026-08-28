package okio;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class AsyncTimeoutTest {
    @Test
    void testBasicEnterAndExit() throws InterruptedException {
        MockAsyncTimeout timeout = new MockAsyncTimeout();
        timeout.enter();
        Assert.assertEquals(timeout.exit(), false);

        timeout.timeout(3, TimeUnit.SECONDS);
        Assert.assertEquals(timeout.isTimedOut(), false);
        timeout.enter();

        Thread.sleep(3100);
        Assert.assertEquals(timeout.isTimedOut(), true);
        Assert.assertTrue(timeout.exit());

        timeout = new MockAsyncTimeout();
        timeout.timeout(3, TimeUnit.SECONDS);
        timeout.enter();
        Thread.sleep(2900);
        Assert.assertFalse(timeout.exit());
        Assert.assertEquals(timeout.isTimedOut(), false);
    }

    @Test
    void testConcurrentEnterAndExit() throws InterruptedException {
        final Random random = new Random(System.currentTimeMillis());
        final AsyncTimeout.Slot slot = new AsyncTimeout.Slot(false);
        final CyclicBarrier barrier = new CyclicBarrier(10);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    long start = System.nanoTime();
                    for (int j = 1; j <= 1000; j++) {
                        MockAsyncTimeout timeout = new MockAsyncTimeout(slot, 0);
                        long timeoutSeconds = random.nextInt(1000);
                        if (timeoutSeconds <= 0) {
                            timeoutSeconds = 1;
                        }
                        if(j %2==0){
                            timeout.timeout(timeoutSeconds, TimeUnit.SECONDS);
                            timeout.enter();
                            Thread.sleep(random.nextInt(10));
                        }else{
                            timeout.deadlineNanoTime(start + TimeUnit.SECONDS.toNanos(timeoutSeconds));
                            timeout.enter();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int count = slot.checkAndPrintSlot();
        Assert.assertEquals(count, 10000);
    }

    @Test
    void testConcurrentTimeout() throws InterruptedException {
        final AsyncTimeout.Slot slot = new AsyncTimeout.Slot(true);
        final CyclicBarrier barrier = new CyclicBarrier(10);
        Thread[] threads = new Thread[10];
        final long now = System.nanoTime();
        final ConcurrentLinkedQueue<MockAsyncTimeout> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 1; j <= 10; j++) {
                        MockAsyncTimeout timeout = new MockAsyncTimeout(slot, j);
                        queue.add(timeout);
                        timeout.deadlineNanoTime(now + TimeUnit.SECONDS.toNanos(j));
                        timeout.enter();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Thread.sleep(5000);
        for(MockAsyncTimeout timeout : queue){
            if(timeout.getTimeoutSeconds() <= 5){
                Assert.assertEquals(timeout.isTimedOut(), true);
                Assert.assertTrue(timeout.exit());
            }else{
                Assert.assertEquals(timeout.isTimedOut(), false);
                Assert.assertFalse(timeout.exit());
            }
        }
    }

    @Test
    void testDaemonThreadGc() throws InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(10);
        Thread[] threads = new Thread[10];
        Random random = new Random(System.currentTimeMillis());
        AsyncTimeout.Slot slot = new AsyncTimeout.Slot(true);
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    final AsyncTimeout asyncTimeout = new AsyncTimeout(slot);
                    asyncTimeout.timeout(random.nextInt(3),TimeUnit.SECONDS);
                    asyncTimeout.enter();
                }catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        threads = new Thread[rootGroup.activeCount()];
        rootGroup.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isDaemon() && "Watchdog".equals(thread.getName())) {
                Assert.assertTrue(
                        thread.getState() == Thread.State.TIMED_WAITING ||
                                thread.getState() == Thread.State.RUNNABLE
                );
            }
        }
        // 等待超时
        try {
            Thread.sleep(71000);
            for (Thread thread : threads) {
                if (thread != null && thread.isDaemon() && "Watchdog".equals(thread.getName())) {
                    // todo zdh fix
//                    Assert.assertSame(thread.getState(), Thread.State.TERMINATED);
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static class MockAsyncTimeout extends AsyncTimeout {
        private boolean flag = false;
        private long timeoutSeconds;

        private MockAsyncTimeout() {

        }

        private MockAsyncTimeout(AsyncTimeout.Slot slot, long timeoutSeconds) {
            super(slot);
            this.timeoutSeconds = timeoutSeconds;
        }

        @Override
        protected void timeOut() {
            this.flag = true;
        }

        boolean isTimedOut() {
            return this.flag;
        }

        long getTimeoutSeconds() {
            return this.timeoutSeconds;
        }

        @Override
        public String toString() {
            return "MockAsyncTimeout{" +
                    "flag=" + flag +
                    ", timeoutSeconds=" + timeoutSeconds +
                    '}';
        }
    }
}
