package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class RestoreObjectOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public RestoreObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "RestoreObjectOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
