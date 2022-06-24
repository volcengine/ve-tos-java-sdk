package com.volcengine.tos.io;

import com.volcengine.tos.model.object.TosObjectInputStream;
import com.volcengine.tos.util.TosClientRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TosRepeatableFileInputStream extends TosObjectInputStream {
    private File f;
    private FileChannel fileChannel;
    private FileInputStream fileInputStream;
    private long markPos = 0;
    public TosRepeatableFileInputStream(File file) throws FileNotFoundException {
        this(new FileInputStream(file), file);
    }
    public TosRepeatableFileInputStream(FileInputStream in) {
        this(in, null);
    }
    public TosRepeatableFileInputStream(FileInputStream fis, File file) {
        super(fis);
        this.f = file;
        this.fileInputStream = fis;
        this.fileChannel = fis.getChannel();
        try {
            this.markPos = fileChannel.position();
        } catch (IOException e) {
            throw new TosClientRuntimeException("Failed to get file position", e);
        }
    }

    @Override
    public int read() throws IOException {
        return fileInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return fileInputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return fileInputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return fileInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return fileInputStream.available();
    }

    @Override
    public void close() throws IOException {
        fileInputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        try {
            markPos = fileChannel.position();
        } catch (IOException e) {
            throw new TosClientRuntimeException("Failed to mark the file position", e);
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        fileChannel.position(markPos);
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
