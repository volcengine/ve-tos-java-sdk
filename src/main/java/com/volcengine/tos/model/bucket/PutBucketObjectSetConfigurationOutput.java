package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutBucketObjectSetConfigurationOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutBucketObjectSetConfigurationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketObjectSetConfigurationOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
