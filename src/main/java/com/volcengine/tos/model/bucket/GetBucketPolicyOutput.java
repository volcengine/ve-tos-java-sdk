package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class GetBucketPolicyOutput {
    private RequestInfo requestInfo;
    private String policy;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public GetBucketPolicyOutput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketPolicyOutput{" +
                "requestInfo=" + requestInfo +
                ", policy='" + policy + '\'' +
                '}';
    }
}
