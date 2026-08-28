package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class DeleteQosPolicyOutput {
    RequestInfo requestInfo;

    public DeleteQosPolicyOutput() {
    }

    public DeleteQosPolicyOutput(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteQosPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteQosPolicyOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}