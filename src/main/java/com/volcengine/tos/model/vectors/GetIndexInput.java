package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class GetIndexInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;
    
    @JsonIgnore
    private String accountId;
    
    @JsonProperty("indexName")
    private String indexName;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public GetIndexInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public GetIndexInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public GetIndexInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;

        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public GetIndexInput build() {
            GetIndexInput input = new GetIndexInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            return input;
        }
    }

    @Override
    public String toString() {
        return "GetIndexInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                '}';
    }
}