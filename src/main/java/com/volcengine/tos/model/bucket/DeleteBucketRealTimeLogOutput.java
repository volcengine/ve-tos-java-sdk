package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class DeleteBucketRealTimeLogOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteBucketRealTimeLogOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketRealTimeLogOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
