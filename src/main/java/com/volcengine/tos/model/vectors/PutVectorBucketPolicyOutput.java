package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class PutVectorBucketPolicyOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutVectorBucketPolicyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RequestInfo requestInfo;

        public Builder requestInfo(RequestInfo requestInfo) {
            this.requestInfo = requestInfo;
            return this;
        }

        public PutVectorBucketPolicyOutput build() {
            PutVectorBucketPolicyOutput output = new PutVectorBucketPolicyOutput();
            output.setRequestInfo(requestInfo);
            return output;
        }
    }
}