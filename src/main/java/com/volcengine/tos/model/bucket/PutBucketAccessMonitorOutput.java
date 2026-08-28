package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketAccessMonitorOutput {
    private RequestInfo requestInfo;

    public PutBucketAccessMonitorOutput(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketAccessMonitorOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketAccessMonitorOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}