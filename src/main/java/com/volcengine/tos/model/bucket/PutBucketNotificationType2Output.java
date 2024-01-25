package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketNotificationType2Output {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketNotificationType2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketNotificationType2Output{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
