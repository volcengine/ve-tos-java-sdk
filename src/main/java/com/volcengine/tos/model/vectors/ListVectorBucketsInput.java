package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class ListVectorBucketsInput extends GenericInput {
    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("maxResults")
    private Integer maxResults;

    public String getPrefix() {
        return prefix;
    }

    public ListVectorBucketsInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVectorBucketsInput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    @Override
    public String toString() {
        return "ListVectorBucketsInput{" +
                ", prefix='" + prefix + '\'' +
                ", nextToken='" + nextToken + '\'' +
                ", maxResults=" + maxResults +
                '}';
    }

    public ListVectorBucketsInput setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String prefix;
        private String nextToken;
        private Integer maxResults;

        private Builder() {
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder nextToken(String nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public ListVectorBucketsInput build() {
            ListVectorBucketsInput listVectorBucketsInput = new ListVectorBucketsInput();
            listVectorBucketsInput.setPrefix(prefix);
            listVectorBucketsInput.setNextToken(nextToken);
            listVectorBucketsInput.setMaxResults(maxResults);
            return listVectorBucketsInput;
        }
    }
}