package com.volcengine.tos.internal;

import com.volcengine.tos.comm.ratelimit.RateLimitRes;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.ratelimit.DefaultRateLimiter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RateLimiterTest {
    @Test
    void rateLimitParamTest() {
        DefaultRateLimiter rateLimiter = new DefaultRateLimiter(1);
        Assert.assertEquals(rateLimiter.getRate(), Consts.DEFAULT_MIN_RATE_LIMITER_RATE);
        Assert.assertEquals(rateLimiter.getCapacity(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY);
        Assert.assertEquals(rateLimiter.getCurrentAmount(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY);

        rateLimiter = new DefaultRateLimiter(Consts.DEFAULT_MIN_RATE_LIMITER_RATE + 1);
        Assert.assertEquals(rateLimiter.getRate(), Consts.DEFAULT_MIN_RATE_LIMITER_RATE + 1);
        Assert.assertEquals(rateLimiter.getCapacity(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY);
        Assert.assertEquals(rateLimiter.getCurrentAmount(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY);

        rateLimiter = new DefaultRateLimiter(Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);
        Assert.assertEquals(rateLimiter.getRate(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);
        Assert.assertEquals(rateLimiter.getCapacity(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);
        Assert.assertEquals(rateLimiter.getCurrentAmount(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);

        rateLimiter = new DefaultRateLimiter(Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1, Consts.DEFAULT_MIN_RATE_LIMITER_RATE + 1);
        Assert.assertEquals(rateLimiter.getRate(), Consts.DEFAULT_MIN_RATE_LIMITER_RATE + 1);
        Assert.assertEquals(rateLimiter.getCapacity(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);
        Assert.assertEquals(rateLimiter.getCurrentAmount(), Consts.DEFAULT_MIN_RATE_LIMITER_CAPACITY + 1);
    }

    @Test
    void rateLimitTest() throws InterruptedException {
        RateLimiter rateLimiter = new DefaultRateLimiter(20 * 1000);
        RateLimitRes res = rateLimiter.acquire(20 * 1000 + 128);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(-1);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertFalse(res.isOk());
        // (want-current) * 1e9 / capacity
        // (10*1000-0) * 1e9 / (20 * 1000)
        Assert.assertEquals(res.getTimeToWaitNanos(), 5 * 1e8);
        Thread.sleep(500);
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());

        rateLimiter = new DefaultRateLimiter(20 * 1000, 10 * 1000);
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertFalse(res.isOk());
        // (want-current) * 1e9 / rate
        // (10*1000-0) * 1e9 / (10 * 1000)
        Assert.assertEquals(res.getTimeToWaitNanos(), 1e9);
        Thread.sleep(1000);
        res = rateLimiter.acquire(10 * 1000);
        Assert.assertTrue(res.isOk());
        Thread.sleep(2000);
        res = rateLimiter.acquire(20 * 1000);
        Assert.assertTrue(res.isOk());
    }
}
