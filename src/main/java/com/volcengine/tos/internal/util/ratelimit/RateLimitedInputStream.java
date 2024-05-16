package com.volcengine.tos.internal.util.ratelimit;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.ratelimit.RateLimitRes;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.model.RetryCountNotifier;
import com.volcengine.tos.internal.util.ParamsChecker;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RateLimitedInputStream extends FilterInputStream implements RetryCountNotifier {
    private final RateLimiter rateLimiter;
    private int acquireN;
    public RateLimitedInputStream(InputStream in, RateLimiter rateLimiter) {
        super(in);
        this.rateLimiter = rateLimiter;
    }

    @Override
    public int read() throws IOException {
        acquireFixed(1);
        int n = super.read();
        int readCount = (n == -1) ? 0 : 1;
        this.acquireN -= readCount;
        return n;
    }

    @Override
    public int read(byte[] b) throws IOException {
        acquireFixed(b.length);
        int n = super.read(b);
        this.acquireN -= n;
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        acquireFixed(Math.min(b.length-off, len));
        int n = super.read(b, off, len);
        this.acquireN -= n;
        return n;
    }

    private void acquireFixed(int want) {
        if (want > this.acquireN) {
            want = want - this.acquireN;
            acquire(want);
            this.acquireN += want;
        }
    }

    private void acquire(long want) {
        ParamsChecker.ensureNotNull(this.rateLimiter, "RateLimiter");
        RateLimitRes res = this.rateLimiter.acquire(want);
        while (res != null && !res.isOk()) {
            try{
                long timeToSleepNanos = res.getTimeToWaitNanos();
                long timeMills = (long) (timeToSleepNanos / 1e6);
                long timeNanos = (long) (timeToSleepNanos % 1e6);
                Thread.sleep(timeMills, (int) timeNanos);
                res = this.rateLimiter.acquire(want);
            } catch (InterruptedException e) {
                throw new TosClientException("tos: rateLimiter sleep interrupted", e);
            }
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        this.acquireN = 0;
        super.reset();
    }

    @Override
    public void setRetryCount(int retryCount) {
        if (this.in instanceof RetryCountNotifier) {
            ((RetryCountNotifier) this.in).setRetryCount(retryCount);
        }
    }
}
