package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class GetVectorBucketPolicyInput extends GenericInput {
    @JsonIgnore
    private String vectorBucketName;
    
    @JsonProperty("vectorBucketName")
    private String vectorBucketNameField;
    
    @JsonIgnore
    private String accountId;
    
    public String getVectorBucketName() {
        return vectorBucketName;
    }
    
    public GetVectorBucketPolicyInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        this.vectorBucketNameField = vectorBucketName;
        return this;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public GetVectorBucketPolicyInput setAccountId(String accountId) {
        this.accountId = accountId;
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
        
        public GetVectorBucketPolicyInput build() {
            GetVectorBucketPolicyInput input = new GetVectorBucketPolicyInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            return input;
        }
    }
}