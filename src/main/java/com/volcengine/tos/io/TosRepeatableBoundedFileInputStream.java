package com.volcengine.tos.io;

import com.volcengine.tos.model.object.TosObjectInputStream;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * TosRepeatableBoundedFileInputStream 用于读取大文件，分段上传
 */
public class TosRepeatableBoundedFileInputStream extends TosObjectInputStream {
    private TosRepeatableFileInputStream in;
    private long totalSize;
    private long readSize;
    private long mark = -1;

    public TosRepeatableBoundedFileInputStream(FileInputStream in, long size){
        super(in);
        this.in = new TosRepeatableFileInputStream(in);
        if (size < 0) {
            throw new IllegalArgumentException("TosRepeatableBoundedFileInputStream input size is invalid, please make it not less than 0");
        }
        this.totalSize = size;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        if (totalSize >= 0 && readSize >= totalSize) {
            return -1;
        }
        int n = in.read();
        readSize++;
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (totalSize >= 0 && readSize >= totalSize) {
            return -1;
        }
        int toRead = len;
        if (totalSize >= 0) {
            toRead = Math.min(len, (int)(totalSize - readSize));
        }
        int n = in.read(b, off, toRead);
        if (n > -1) {
            readSize += n;
        }
        return n;
    }

    @Override
    public int available() throws IOException {
        if (totalSize >= 0 && readSize >= totalSize) {
            return -1;
        }
        return in.available();
    }

    @Override
    public long skip(long n) throws IOException {
        long toSkip = totalSize >= 0 ? Math.min(n, totalSize - readSize) : n;
        long skipped = in.skip(toSkip);
        readSize += skipped;
        return skipped;
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
        mark = readSize;
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        readSize = mark;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
