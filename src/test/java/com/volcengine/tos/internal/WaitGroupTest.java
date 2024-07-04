package com.volcengine.tos.internal;

import com.volcengine.tos.internal.util.IWaitGroup;
import com.volcengine.tos.internal.util.WaitGroup;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

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
}
