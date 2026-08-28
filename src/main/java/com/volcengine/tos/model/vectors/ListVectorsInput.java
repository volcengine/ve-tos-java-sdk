package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class ListVectorsInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("maxResults")
    private Integer maxResults;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("returnData")
    private Boolean returnData;

    @JsonProperty("returnMetadata")
    private Boolean returnMetadata;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public ListVectorsInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public ListVectorsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public ListVectorsInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public ListVectorsInput setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVectorsInput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public Boolean getReturnData() {
        return returnData;
    }

    public ListVectorsInput setReturnData(Boolean returnData) {
        this.returnData = returnData;
        return this;
    }

    public Boolean getReturnMetadata() {
        return returnMetadata;
    }

    public ListVectorsInput setReturnMetadata(Boolean returnMetadata) {
        this.returnMetadata = returnMetadata;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;
        private Integer maxResults;
        private String nextToken;
        private Boolean returnData;
        private Boolean returnMetadata;

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

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder nextToken(String nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public Builder returnData(Boolean returnData) {
            this.returnData = returnData;
            return this;
        }

        public Builder returnMetadata(Boolean returnMetadata) {
            this.returnMetadata = returnMetadata;
            return this;
        }

        public ListVectorsInput build() {
            ListVectorsInput input = new ListVectorsInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setMaxResults(maxResults);
            input.setNextToken(nextToken);
            input.setReturnData(returnData);
            input.setReturnMetadata(returnMetadata);
            return input;
        }
    }

    @Override
    public String toString() {
        return "ListVectorsInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", maxResults=" + maxResults +
                ", nextToken='" + nextToken + '\'' +
                ", returnData=" + returnData +
                ", returnMetadata=" + returnMetadata +
                '}';
    }
}