package com.volcengine.tos.comm.io;

import java.io.IOException;

public interface Retryable {
    void reset() throws IOException;
}
