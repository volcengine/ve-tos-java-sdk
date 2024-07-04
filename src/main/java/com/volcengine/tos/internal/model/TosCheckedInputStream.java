package com.volcengine.tos.internal.model;

import com.volcengine.tos.comm.io.Retryable;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CheckedInputStream;

public class TosCheckedInputStream extends CheckedInputStream implements RetryCountNotifier, Retryable {
    /**
     * Creates an input stream using the specified Checksum.
     *
     * @param in    the input stream
     * @param cksum the Checksum
     */
    private TosRepeatableChecksum checksum;
    public TosCheckedInputStream(InputStream in, TosRepeatableChecksum cksum) {
        super(in, cksum);
        this.checksum = cksum;
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
        this.checksum.markCurrentValue();
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        if (this.checksum != null) {
            TosUtils.getLogger().debug("tos: call TosCheckedInputStream reset");
            this.checksum.reset();
        }
    }

    @Override
    public void setRetryCount(int retryCount) {
        if (this.in instanceof RetryCountNotifier) {
            ((RetryCountNotifier) this.in).setRetryCount(retryCount);
        }
    }
}
