package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class AbortMultipartUploadOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public AbortMultipartUploadOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
