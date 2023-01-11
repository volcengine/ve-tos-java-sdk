package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class DeleteBucketWebsiteOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteBucketWebsiteOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketWebsiteOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
