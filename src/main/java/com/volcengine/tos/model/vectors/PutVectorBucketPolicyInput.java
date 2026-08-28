package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutVectorBucketPolicyInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("policy")
    private String policy;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public PutVectorBucketPolicyInput setVectorBucketName(String accountId, String vectorBucketName) {
        this.accountId = accountId;
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPolicy() {
        return policy;
    }

    public PutVectorBucketPolicyInput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String policy;

        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder policy(String policy) {
            this.policy = policy;
            return this;
        }

        public PutVectorBucketPolicyInput build() {
            PutVectorBucketPolicyInput input = new PutVectorBucketPolicyInput();
            input.setVectorBucketName(accountId, vectorBucketName);
            input.setPolicy(policy);
            return input;
        }
    }
}