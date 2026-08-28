package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class DeleteVectorBucketPolicyInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;
    
    public String getVectorBucketName() {
        return vectorBucketName;
    }
    
    public DeleteVectorBucketPolicyInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public DeleteVectorBucketPolicyInput setAccountId(String accountId) {
        this.accountId= accountId;
        return this;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        
        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }
        
        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }
        
        public DeleteVectorBucketPolicyInput build() {
            DeleteVectorBucketPolicyInput input = new DeleteVectorBucketPolicyInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            return input;
        }
    }
}