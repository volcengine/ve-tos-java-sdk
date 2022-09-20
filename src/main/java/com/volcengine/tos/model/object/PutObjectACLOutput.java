package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectACLOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectACLOutput requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectACLOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
