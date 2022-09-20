package com.volcengine.tos.comm.io;

import com.volcengine.tos.model.object.TosObjectInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TosRepeatableInputStream extends TosObjectInputStream {
    private InputStream originInputStream;

    public TosRepeatableInputStream(InputStream in, int bufferSize) {
        super(in);
        // 带了 bufferSize 且非 markSupported 类型的，用 BufferedInputStream 去读
        if (bufferSize > 0 && in != null && !in.markSupported()) {
            originInputStream = new WrappedBufferedInputStream(in, bufferSize);
        } else {
            originInputStream = in;
        }
    }

    @Override
    public int read() throws IOException {
        return originInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return originInputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return originInputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return originInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return originInputStream.available();
    }

    @Override
    public void close() throws IOException {
        if (originInputStream != null) {
            originInputStream.close();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        originInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        originInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return originInputStream.markSupported();
    }

    private static final class WrappedBufferedInputStream extends BufferedInputStream {
        public WrappedBufferedInputStream(InputStream in, int size) {
            super(in, size);
        }
    }

    protected InputStream getOriginInputStream() {
        return this.originInputStream;
    }
}
