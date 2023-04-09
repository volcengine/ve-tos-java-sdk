package com.volcengine.tos.internal.util.aborthook;

import okio.Source;

import java.io.IOException;
import java.io.InputStream;

public class DefaultAbortTosObjectInputStreamHook implements AbortInputStreamHook {
    private final InputStream inputStream;
    private final Source source;
    private boolean isAborted;

    public DefaultAbortTosObjectInputStreamHook(InputStream inputStream, Source source) {
        this.inputStream = inputStream;
        this.source = source;
        this.isAborted = false;
    }

    /**
     * abort() closes the object content immediately.
     *
     * The okhttp client attempts to exhaust {@code source} in 100 milliseconds while calling close() method,
     * which is helpful for reusing the socket connection.
     * But in some specific cases, this attempt will be useless.
     * For example, if we only need to read a piece of data from a large inputStream,
     * and then call {@code close()} to close it, the {@code close()} will still block for 100ms
     * to read the rest data, which is a meaningless cost for users.
     * If you want to close the inputStream immediately, you can call this {@code abort()} method,
     * and notice that the socket connection will be released and not be reused by okhttp.
     */
    @Override
    public void abort() throws IOException {
        if (isAborted) {
            return;
        }
        if (inputStream != null && source != null) {
            this.source.timeout().deadlineNanoTime(System.nanoTime());
            this.isAborted = true;
            inputStream.close();
        }
    }
}
