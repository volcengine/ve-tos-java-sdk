package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class PointCloudCompressOutput implements Closeable {
    @JsonIgnore
    private RequestInfo requestInfo;

    private InputStream content;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PointCloudCompressOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public PointCloudCompressOutput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
        }
    }

    @Override
    public String toString() {
        return "PointCloudCompressOutput{" +
                "requestInfo=" + requestInfo +
                ", content=" + content +
                '}';
    }
}
