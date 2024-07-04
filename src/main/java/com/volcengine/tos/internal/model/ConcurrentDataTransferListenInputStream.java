package com.volcengine.tos.internal.model;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.internal.Consts;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentDataTransferListenInputStream extends DataTransferListenInputStream {
    private final DataTransferListener listener;
    private long totalBytes;
    private long subConsumedBytes;
    private AtomicLong consumedBytes;
    private int unNotifiedBytes;
    private boolean doneEOF;
    private long markedSubConsumedBytes;
    private int markedUnNotifiedBytes;

    public ConcurrentDataTransferListenInputStream(InputStream is, DataTransferListener listener, long total, AtomicLong consumed) {
        super(is);
        if (is == null || listener == null || consumed == null) {
            throw new TosClientException("invalid input", null);
        }
        this.listener = listener;
        this.totalBytes = total;
        this.consumedBytes = consumed;
    }

    @Override
    public int read() throws IOException {
        int n = super.read();
        if (n != -1) {
            bytesRead(1);
        } else {
            eof();
        }
        return n;
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
        markedSubConsumedBytes = subConsumedBytes;
        markedUnNotifiedBytes = unNotifiedBytes;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        unNotifiedBytes = markedUnNotifiedBytes;
        long old = consumedBytes.get();
        consumedBytes.compareAndSet(old, old-(subConsumedBytes-markedSubConsumedBytes));
        subConsumedBytes = markedSubConsumedBytes;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = super.read(b, off, len);
        if (n != -1) {
            bytesRead(n);
        } else {
            eof();
        }
        return n;
    }

    @Override
    public void close() throws IOException {
        eof();
        super.close();
    }

    private void onEOF() {
        // post the last unnotified bytes
        if (unNotifiedBytes > 0) {
            calculateBytesRead();
        }
    }

    private void calculateBytesRead() {
        subConsumedBytes += unNotifiedBytes;
        consumedBytes.addAndGet(unNotifiedBytes);
        onBytesRead(unNotifiedBytes);
        unNotifiedBytes = 0;
    }

    // 断点续传场景，RetryCount 字段无实际意义
    private void onBytesRead(int bytesRead) {
        DataTransferStatus status = new DataTransferStatus().setType(DataTransferType.DATA_TRANSFER_RW)
                .setTotalBytes(totalBytes).setConsumedBytes(consumedBytes.get()).setRwOnceBytes(bytesRead).setRetryCount(-1);
        listener.dataTransferStatusChange(status);
    }

    private void bytesRead(int bytesRead) {
        unNotifiedBytes += bytesRead;
        if (unNotifiedBytes >= Consts.DEFAULT_PROGRESS_CALLBACK_SIZE) {
            calculateBytesRead();
        }
    }

    private void eof() {
        // take eof as success
        if (doneEOF) {
            return;
        }
        onEOF();
        unNotifiedBytes = 0;
        doneEOF = true;
    }

}
