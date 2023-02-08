package com.volcengine.tos.internal.util.ratelimit;

import com.volcengine.tos.comm.ratelimit.RateLimitRes;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.Consts;

public class DefaultRateLimiter implements RateLimiter {
    private long capacity;
    private long rate;
    private long lastTokenGivenNanos;
    private long currentAmount;

    public DefaultRateLimiter(long rate) {
        this.capacity = rate;
        this.rate = rate;
        this.lastTokenGivenNanos = System.nanoTime();
        validateRateInput();
        this.currentAmount = this.capacity;
    }

    public DefaultRateLimiter(long capacity, long rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.lastTokenGivenNanos = System.nanoTime();
        validateRateInput();
        this.currentAmount = this.capacity;
    }

    private void validateRateInput() {
        if (this.capacity < Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY) {
            this.capacity = Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY;
        }
        if (this.rate < Consts.DEFAULT_MIN_RATE_LIMITER_RATE) {
            this.rate = Consts.DEFAULT_MIN_RATE_LIMITER_RATE;
        }
    }

    @Override
    public RateLimitRes acquire(long want) {
        if (want <= 0) {
            return new RateLimitRes().setOk(true).setTimeToWaitNanos(0);
        }
        synchronized (this) {
            return reserveAndGetWaitTimeMills(want, System.nanoTime());
        }
    }

    private RateLimitRes reserveAndGetWaitTimeMills(long want, long timeMicros) {
        double duration = (timeMicros - lastTokenGivenNanos) / 1e9;
        long increaseToken = (long) (duration * rate);
        currentAmount = Math.min(increaseToken + currentAmount, capacity);
        if (want > capacity) {
            return new RateLimitRes().setOk(true).setTimeToWaitNanos(0);
        }
        if (want > currentAmount) {
            long timeToWait = (long)((want - currentAmount) * 1e9 / rate);
            return new RateLimitRes().setOk(false).setTimeToWaitNanos(timeToWait);
        }
        lastTokenGivenNanos = timeMicros;
        currentAmount -= want;
        return new RateLimitRes().setOk(true).setTimeToWaitNanos(0);
    }

    public long getCapacity() {
        return capacity;
    }

    public DefaultRateLimiter setCapacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    public long getRate() {
        return rate;
    }

    public DefaultRateLimiter setRate(long rate) {
        this.rate = rate;
        return this;
    }

    public long getLastTokenGivenNanos() {
        return lastTokenGivenNanos;
    }

    public DefaultRateLimiter setLastTokenGivenNanos(long lastTokenGivenNanos) {
        this.lastTokenGivenNanos = lastTokenGivenNanos;
        return this;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public DefaultRateLimiter setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
        return this;
    }
}
