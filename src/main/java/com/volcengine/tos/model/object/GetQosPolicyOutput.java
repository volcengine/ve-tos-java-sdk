package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class GetQosPolicyOutput {
    private RequestInfo requestInfo;
    private String policy;

    public GetQosPolicyOutput() {
    }

    public GetQosPolicyOutput(RequestInfo requestInfo, String policy) {
        this.requestInfo = requestInfo;
        this.policy = policy;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetQosPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public GetQosPolicyOutput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    @Override
    public String toString() {
        return "GetQosPolicyOutput{" +
                "requestInfo=" + requestInfo +
                ", policy='" + policy + '\'' +
                '}';
    }
}