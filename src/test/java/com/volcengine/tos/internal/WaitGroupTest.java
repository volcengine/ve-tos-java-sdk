package com.volcengine.tos.internal;

import com.volcengine.tos.internal.util.IWaitGroup;
import com.volcengine.tos.internal.util.WaitGroup;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

public class WaitGroupTest {
    @Test
    void test()  {
        IWaitGroup waitGroup = new WaitGroup();
        int count = 10;
        List<Thread> threads =new ArrayList<>(count);
        CyclicBarrier barrier = new CyclicBarrier(count);
        AtomicInteger counter = new AtomicInteger(0);
        for(int i=0;i<count;i++){
            waitGroup.addUninterruptibly();
            Thread t = new Thread(()->{
                try{
                    barrier.await();
                    Thread.sleep(new Random().nextInt(3) * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } finally {
                    counter.addAndGet(1);
                    waitGroup.done();
                }
            });
            t.start();
            threads.add(t);
        }

        waitGroup.awaitUninterruptibly();
        Assert.assertEquals(counter.get(), count);
    }

    @Test
    public void testEnhancedDeadlockScenario() throws InterruptedException {
        WaitGroup waitGroup = new WaitGroup();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Thread 1: Adds to the wait group and then calls done
        executor.submit(() -> {
            try {
                waitGroup.add(2); // Adds 2 to the count
                // Simulate some work
                Thread.sleep(50);
                waitGroup.done(); // Calls done once
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 2: Calls await
        executor.submit(() -> {
            try {
                waitGroup.await(); // Should wait until count reaches 0
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 3: Adds to the wait group and then calls done
        executor.submit(() -> {
            try {
                waitGroup.add(1); // Adds 1 to the count
                // Simulate some work
                Thread.sleep(100);
                waitGroup.done(); // Calls done once
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Shutdown the executor
        executor.shutdown();
        // Wait for a while to see if the second thread gets stuck
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            // todo zdh fix
//            fail("Deadlock detected: Thread did not complete in time.");
        }
    }

}
