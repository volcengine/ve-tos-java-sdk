package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class AppendObjectOutput {
    private RequestInfo requestInfo;
    private String etag;
    private long nextAppendOffset;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public AppendObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public AppendObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getNextAppendOffset() {
        return nextAppendOffset;
    }

    public AppendObjectOutput setNextAppendOffset(long nextAppendOffset) {
        this.nextAppendOffset = nextAppendOffset;
        return this;
    }

    @Override
    public String toString() {
        return "AppendObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", etag='" + etag + '\'' +
                ", nextAppendOffset=" + nextAppendOffset +
                '}';
    }
}
