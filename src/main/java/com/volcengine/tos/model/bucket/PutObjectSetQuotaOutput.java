package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectSetQuotaOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectSetQuotaOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetQuotaOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
