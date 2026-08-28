package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class PutQosPolicyOutput {
    private RequestInfo requestInfo;

    public PutQosPolicyOutput() {
    }

    public PutQosPolicyOutput(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutQosPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutQosPolicyOutput{"
                + "requestInfo=" + requestInfo+
                '}';
    }
}
