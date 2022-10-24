package com.volcengine.tos.internal.util.ratelimit;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.ratelimit.RateLimitRes;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.ParamsChecker;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RateLimitedInputStream extends FilterInputStream {
    private RateLimiter rateLimiter;
    public RateLimitedInputStream(InputStream in, RateLimiter rateLimiter) {
        super(in);
        this.rateLimiter = rateLimiter;
    }

    @Override
    public int read() throws IOException {
        acquire(1);
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (b != null) {
            acquire(b.length);
        }
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b != null) {
            acquire(Math.min(b.length, len));
        }
        return super.read(b, off, len);
    }

    private void acquire(long want) {
        ParamsChecker.ensureNotNull(this.rateLimiter, "RateLimiter is null");
        RateLimitRes res = this.rateLimiter.acquire(want);
        if (res != null) {
            while (!res.isOk()) {
                try{
                    long timeToSleepNanos = res.getTimeToWaitNanos();
                    long timeMills = (long) (timeToSleepNanos / 1e6);
                    long timeNanos = (long) (timeToSleepNanos % 1e6);
//                    System.out.printf("time to sleep: %d, %d \n", timeMills, timeNanos);
                    Thread.sleep(timeMills, (int) timeNanos);
                    res = this.rateLimiter.acquire(want);
                } catch (InterruptedException e) {
                    throw new TosClientException("tos: rateLimiter sleep interrupted", e);
                }
            }
        }
    }
}
