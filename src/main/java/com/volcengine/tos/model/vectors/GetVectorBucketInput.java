package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class GetVectorBucketInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;
    
    @JsonIgnore
    private String accountId;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public GetVectorBucketInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public GetVectorBucketInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return "GetVectorBucketInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String vectorBucketName;
        private String accountId;

        private Builder() {
        }

        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public GetVectorBucketInput build() {
            GetVectorBucketInput getVectorBucketInput = new GetVectorBucketInput();
            getVectorBucketInput.setVectorBucketName(vectorBucketName);
            getVectorBucketInput.setAccountId(accountId);
            return getVectorBucketInput;
        }
    }
}