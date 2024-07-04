package com.volcengine.tos.internal.util;

public interface IWaitGroup {
    void add() throws InterruptedException;

    void add(int n) throws InterruptedException;

    void done();

    void done(int n);

    void await() throws InterruptedException;

    void addUninterruptibly();

    void addUninterruptibly(int n);

    void awaitUninterruptibly();
}
