package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectSetOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectSetOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
