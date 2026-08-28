package com.volcengine.tos.internal.util;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class WaitGroup implements IWaitGroup {
    private final AtomicLong al;
    private final Lock lock;
    private final Condition cond;
    public WaitGroup() {
        this.al = new AtomicLong(0);
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
    }
    public void addUninterruptibly() {
        this.addUninterruptibly(1);
    }
    public void addUninterruptibly(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        this.al.addAndGet(n);
    }
    public void awaitUninterruptibly() {
        this.lock.lock();
        try {
            while (al.get() != 0) {
                this.cond.awaitUninterruptibly();
            }
        } finally {
            this.lock.unlock();
        }
    }
    public void add() throws InterruptedException {
        this.add(1);
    }
    public void add(int n) throws InterruptedException {
        if (n <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        this.al.addAndGet(n);
    }
    public void await() throws InterruptedException {
        this.lock.lock();
        try {
            while (al.get() != 0) {
                this.cond.await();
            }
        } finally {
            this.lock.unlock();
        }
    }
    public void done() {
        this.done(1);
    }
    public void done(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        long newValue = this.al.addAndGet(-n);
        if (newValue < 0) {
            throw new IllegalStateException("Count has gone negative");
        }
        if (newValue == 0) {
            this.lock.lock();
            try {
                this.cond.signalAll();
            } finally {
                this.lock.unlock();
            }
        }
    }
}