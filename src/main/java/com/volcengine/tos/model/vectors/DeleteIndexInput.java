package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class DeleteIndexInput extends GenericInput {
    @JsonIgnore
    private String vectorBucketName;
    
    @JsonIgnore
    private String accountId;
    
    @JsonProperty("indexName")
    private String indexName;
    
    @JsonProperty("vectorBucketName")
    private String vectorBucketNameInBody;
    
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
        
        public DeleteIndexInput build() {
            DeleteIndexInput input = new DeleteIndexInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setVectorBucketNameInBody(vectorBucketName);
            return input;
        }
    }
    
    public String getVectorBucketName() {
        return vectorBucketName;
    }
    
    public DeleteIndexInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        this.vectorBucketNameInBody = vectorBucketName;
        return this;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public DeleteIndexInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public String getIndexName() {
        return indexName;
    }
    
    public DeleteIndexInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }
    
    public String getVectorBucketNameInBody() {
        return vectorBucketNameInBody;
    }
    
    public DeleteIndexInput setVectorBucketNameInBody(String vectorBucketNameInBody) {
        this.vectorBucketNameInBody = vectorBucketNameInBody;
        return this;
    }
    
    @Override
    public String toString() {
        return "DeleteIndexInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                '}';
    }
}