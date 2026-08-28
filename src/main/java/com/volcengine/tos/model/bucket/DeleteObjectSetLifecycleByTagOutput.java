package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class DeleteObjectSetLifecycleByTagOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteObjectSetLifecycleByTagOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectSetLifecycleByTagOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
