package com.volcengine.tos.internal.util.aborthook;

import java.io.IOException;

public interface AbortInputStreamHook {
    void abort() throws IOException;
}
