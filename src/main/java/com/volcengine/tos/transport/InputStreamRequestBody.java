package com.volcengine.tos.transport;

import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.io.TosRepeatableInputStream;
import com.volcengine.tos.model.object.TosObjectInputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class InputStreamRequestBody extends RequestBody implements Closeable {
    private InputStream inputStream;
    private final MediaType contentType;
    private long contentLength;

    InputStreamRequestBody(MediaType contentType, InputStream inputStream, long contentLength) {
        Objects.requireNonNull(inputStream, "inputStream is null");
        this.contentType = contentType;
        this.contentLength = contentLength;
        if (this.contentLength < -1L) {
            this.contentLength = -1L;
        }
        this.inputStream = inputStream;
        if (!(this.inputStream instanceof TosObjectInputStream)) {
            // 继承自 TosObjectInputStream 的 inputStream，已经实现 repeatable 特性
            this.inputStream = new TosRepeatableInputStream(inputStream, Consts.DEFAULT_READ_BUFFER_SIZE);
        }
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        // add time cost log
        Source source = null;
        try {
            source = Okio.source(inputStream);
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }

    protected InputStream getInputStream() {
        return this.inputStream;
    }
}
