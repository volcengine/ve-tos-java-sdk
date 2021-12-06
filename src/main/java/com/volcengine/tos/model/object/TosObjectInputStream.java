package com.volcengine.tos.model.object;

import java.io.FilterInputStream;
import java.io.InputStream;

public class TosObjectInputStream extends FilterInputStream {

    public TosObjectInputStream(InputStream in) {
        super(in);
    }

}
