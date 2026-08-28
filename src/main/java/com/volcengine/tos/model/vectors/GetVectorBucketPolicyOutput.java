package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class GetVectorBucketPolicyOutput {
    private String policy;
    
    @JsonIgnore
    private RequestInfo requestInfo;

    public String getPolicy() {
        return policy;
    }

    @Override
    public String toString() {
        return "GetVectorBucketPolicyOutput{" +
                "policy='" + policy + '\'' +
                ", requestInfo=" + requestInfo +
                '}';
    }

    public GetVectorBucketPolicyOutput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
    
    public GetVectorBucketPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }
}