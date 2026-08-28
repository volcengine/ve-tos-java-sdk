package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectSetLifecycleOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectSetLifecycleOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetLifecycleOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
