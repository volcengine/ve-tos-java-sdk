package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class ListIndexesInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;
    
    @JsonIgnore
    private String accountId;
    
    @JsonProperty("maxResults")
    private Integer maxResults;
    
    @JsonProperty("nextToken")
    private String nextToken;
    
    @JsonProperty("prefix")
    private String prefix;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public ListIndexesInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public ListIndexesInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public ListIndexesInput setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListIndexesInput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListIndexesInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private Integer maxResults;
        private String nextToken;
        private String prefix;

        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
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

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListIndexesInput build() {
            ListIndexesInput input = new ListIndexesInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setMaxResults(maxResults);
            input.setNextToken(nextToken);
            input.setPrefix(prefix);
            return input;
        }
    }

    @Override
    public String toString() {
        return "ListIndexesInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", maxResults=" + maxResults +
                ", nextToken='" + nextToken + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}