package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class DeleteVectorBucketPolicyOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteVectorBucketPolicyOutput setRequestInfo(RequestInfo requestInfo) {
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

        public DeleteVectorBucketPolicyOutput build() {
            DeleteVectorBucketPolicyOutput output = new DeleteVectorBucketPolicyOutput();
            output.setRequestInfo(requestInfo);
            return output;
        }
    }
}