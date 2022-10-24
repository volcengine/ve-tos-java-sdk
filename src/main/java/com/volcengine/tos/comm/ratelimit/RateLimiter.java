package com.volcengine.tos.comm.ratelimit;

public interface RateLimiter {
    RateLimitRes acquire(long want);
}
