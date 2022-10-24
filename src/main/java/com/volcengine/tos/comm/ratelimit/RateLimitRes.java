package com.volcengine.tos.comm.ratelimit;

public class RateLimitRes {
    private boolean ok;
    private long timeToWaitNanos;

    public boolean isOk() {
        return ok;
    }

    public RateLimitRes setOk(boolean ok) {
        this.ok = ok;
        return this;
    }

    public long getTimeToWaitNanos() {
        return timeToWaitNanos;
    }

    public RateLimitRes setTimeToWaitNanos(long timeToWaitNanos) {
        this.timeToWaitNanos = timeToWaitNanos;
        return this;
    }

    @Override
    public String toString() {
        return "RateLimitRes{" +
                "ok=" + ok +
                ", timeToWaitMills=" + timeToWaitNanos +
                '}';
    }
}
