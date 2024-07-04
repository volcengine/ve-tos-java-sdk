package com.volcengine.tos.internal.util;

public class NullWaitGroup implements IWaitGroup{
    private static final NullWaitGroup INSTANCE = new NullWaitGroup();

    public static NullWaitGroup getInstance() {
        return INSTANCE;
    }

    @Override
    public void add() throws InterruptedException {
    }

    @Override
    public void add(int n) throws InterruptedException {
    }

    @Override
    public void done() {
    }

    @Override
    public void done(int n) {
    }

    @Override
    public void await() throws InterruptedException {
    }

    @Override
    public void addUninterruptibly() {

    }

    @Override
    public void addUninterruptibly(int n) {

    }

    @Override
    public void awaitUninterruptibly() {

    }
}
