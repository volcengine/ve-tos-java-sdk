package com.volcengine.tos.internal.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitGroup implements IWaitGroup {
    private final AtomicLong al;
    private final Lock lock;
    private final Condition cond;
    public WaitGroup(){
        this.al = new AtomicLong(0);
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
    }

    public void addUninterruptibly() {
        this.addUninterruptibly(1);
    }

    public void addUninterruptibly(int n) {
        if (n <= 0) {
            return;
        }
        this.al.addAndGet(n);
    }

    public void awaitUninterruptibly() {
        while (al.get() != 0) {
            this.lock.lock();
            this.cond.awaitUninterruptibly();
            this.lock.unlock();
        }
    }

    public void add() throws InterruptedException {
        this.addUninterruptibly(1);
    }

    public void add(int n) throws InterruptedException {
        this.addUninterruptibly(n);
    }

    public void await() throws InterruptedException {
        while (al.get() != 0) {
            this.lock.lock();
            this.cond.await();
            this.lock.unlock();
        }
    }

    public void done(){
        this.done(1);
    }

    public void done(int n){
        if (n <= 0) {
            return;
        }
        if (al.addAndGet(-n) == 0) {
            this.lock.lock();
            this.cond.signalAll();
            this.lock.unlock();
        }
    }

}
