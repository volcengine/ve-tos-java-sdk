package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class ModifyObjectOutput {
    private RequestInfo requestInfo;
    private long nextModifyOffset;
    private String hashCrc64ecma;
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ModifyObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public long getNextModifyOffset() {
        return nextModifyOffset;
    }

    public ModifyObjectOutput setNextModifyOffset(long nextModifyOffset) {
        this.nextModifyOffset = nextModifyOffset;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public ModifyObjectOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    @Override
    public String toString() {
        return "ModifyObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", nextModifyOffset=" + nextModifyOffset +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                '}';
    }
}
