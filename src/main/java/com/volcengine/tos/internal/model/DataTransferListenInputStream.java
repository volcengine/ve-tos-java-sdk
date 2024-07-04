package com.volcengine.tos.internal.model;

import com.volcengine.tos.comm.io.Retryable;

import java.io.FilterInputStream;
import java.io.InputStream;

public abstract class DataTransferListenInputStream extends FilterInputStream implements RetryCountNotifier, Retryable {
    protected int retryCount;
    protected DataTransferListenInputStream(InputStream in) {
        super(in);
    }

    @Override
    public final void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
