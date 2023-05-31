package com.volcengine.tos.internal.model;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.internal.Consts;

import java.io.IOException;
import java.io.InputStream;

public class SimpleDataTransferListenInputStream extends DataTransferListenInputStream {
    private final DataTransferListener listener;
    private long totalBytes;
    private long consumedBytes;
    private int unNotifiedBytes;
    private boolean readStarted;
    private boolean doneEOF;
    private long markedConsumedBytes;
    private int markedUnNotifiedBytes;

    public SimpleDataTransferListenInputStream(InputStream is, DataTransferListener listener, long total) {
        super(is);
        if (is == null || listener == null) {
            throw new TosClientException("invalid input", null);
        }
        this.listener = listener;
        this.totalBytes = total;
    }

    @Override
    public int read() throws IOException {
        if (!readStarted) {
            onFirstRead();
            readStarted = true;
        }
        try{
            int n = super.read();
            if (n == -1) {
                eof();
            } else {
                bytesRead(1);
            }
            return n;
        } catch (IOException e) {
            onFailed();
            throw e;
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
        markedConsumedBytes = consumedBytes;
        markedUnNotifiedBytes = unNotifiedBytes;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        unNotifiedBytes = markedUnNotifiedBytes;
        consumedBytes = markedConsumedBytes;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!readStarted) {
            onFirstRead();
            readStarted = true;
        }
        try{
            int n = super.read(b, off, len);
            if (n == -1) {
                eof();
            } else {
                bytesRead(n);
            }
            return n;
        } catch (IOException e) {
            onFailed();
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        eof();
        super.close();
    }

    private void onFirstRead() {
        DataTransferStatus status = new DataTransferStatus().setType(DataTransferType.DATA_TRANSFER_STARTED)
                .setTotalBytes(totalBytes).setConsumedBytes(consumedBytes);
        listener.dataTransferStatusChange(status);
    }

    private void onEOF() {
        // post the last unnotified bytes
        if (unNotifiedBytes > 0) {
            consumedBytes += unNotifiedBytes;
            onBytesRead(unNotifiedBytes);
            unNotifiedBytes = 0;
        }
        // post event
        DataTransferStatus status = new DataTransferStatus().setTotalBytes(totalBytes).setConsumedBytes(consumedBytes);
        if (consumedBytes < totalBytes) {
            // unexpected eof
            status = status.setType(DataTransferType.DATA_TRANSFER_FAILED);
        } else {
            status = status.setType(DataTransferType.DATA_TRANSFER_SUCCEED);
        }
        listener.dataTransferStatusChange(status);
    }

    private void onFailed() {
        DataTransferStatus status = new DataTransferStatus().setType(DataTransferType.DATA_TRANSFER_FAILED)
                .setTotalBytes(totalBytes).setConsumedBytes(consumedBytes);
        listener.dataTransferStatusChange(status);
    }

    private void onBytesRead(int bytesRead) {
        DataTransferStatus status = new DataTransferStatus().setType(DataTransferType.DATA_TRANSFER_RW)
                .setTotalBytes(totalBytes).setConsumedBytes(consumedBytes).setRwOnceBytes(bytesRead);
        listener.dataTransferStatusChange(status);
    }

    private void bytesRead(int bytesRead) {
        unNotifiedBytes += bytesRead;
        if (unNotifiedBytes >= Consts.DEFAULT_PROGRESS_CALLBACK_SIZE) {
            consumedBytes += unNotifiedBytes;
            onBytesRead(unNotifiedBytes);
            unNotifiedBytes = 0;
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
