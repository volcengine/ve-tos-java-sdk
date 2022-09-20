package com.volcengine.tos.model.object;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class GetObjectV2Output implements Closeable {
    private GetObjectBasicOutput getObjectBasicOutput;
    private transient InputStream content;

    public GetObjectV2Output(GetObjectBasicOutput getObjectBasicOutput, InputStream content) {
        this.getObjectBasicOutput = getObjectBasicOutput;
        this.content = content;
    }

    public GetObjectBasicOutput getGetObjectBasicOutput() {
        return getObjectBasicOutput;
    }

    public InputStream getContent() {
        return content;
    }

    public GetObjectV2Output setGetObjectBasicOutput(GetObjectBasicOutput getObjectBasicOutput) {
        this.getObjectBasicOutput = getObjectBasicOutput;
        return this;
    }

    public GetObjectV2Output setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
        }
    }
}
