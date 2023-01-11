package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketACLOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketACLOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketACLOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
