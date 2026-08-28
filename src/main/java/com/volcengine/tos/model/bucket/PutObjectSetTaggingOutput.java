package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectSetTaggingOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectSetTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetTaggingOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
