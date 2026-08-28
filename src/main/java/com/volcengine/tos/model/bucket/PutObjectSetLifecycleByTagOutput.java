package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectSetLifecycleByTagOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectSetLifecycleByTagOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetLifecycleByTagOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
