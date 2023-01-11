package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class DeleteBucketCustomDomainOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteBucketCustomDomainOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketCustomDomainOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
