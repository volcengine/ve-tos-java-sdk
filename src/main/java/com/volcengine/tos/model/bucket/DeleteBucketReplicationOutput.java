package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class DeleteBucketReplicationOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteBucketReplicationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketReplicationOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
