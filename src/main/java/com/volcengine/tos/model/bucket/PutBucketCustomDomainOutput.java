package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketCustomDomainOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketCustomDomainOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketCustomDomainOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
