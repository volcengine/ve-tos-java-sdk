package com.volcengine.tos.internal.model;

import java.io.FilterInputStream;
import java.io.InputStream;

public abstract class DataTransferListenInputStream extends FilterInputStream {
    protected DataTransferListenInputStream(InputStream in) {
        super(in);
    }
}
