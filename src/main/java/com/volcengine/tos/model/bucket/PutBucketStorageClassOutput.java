package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketStorageClassOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketStorageClassOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketStorageClassOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
