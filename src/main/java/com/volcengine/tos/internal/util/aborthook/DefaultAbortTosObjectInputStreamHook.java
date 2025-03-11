package com.volcengine.tos.internal.util.aborthook;

import java.io.IOException;
import java.io.InputStream;

public class DefaultAbortTosObjectInputStreamHook implements AbortInputStreamHook {

    private final InputStream inputStream;
    private boolean isAborted;

    public DefaultAbortTosObjectInputStreamHook(InputStream inputStream) {
        this.inputStream = inputStream;
        this.isAborted = false;
    }

    /**
     * abort() closes the object content immediately. do not use the ok http, so
     * close directly
     */
    @Override
    public void abort() throws IOException {
        if (isAborted) {
            return;
        }
        if (inputStream != null) {
            this.isAborted = true;
            inputStream.close();
        }
    }
}
