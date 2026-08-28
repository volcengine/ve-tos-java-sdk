package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class ListAccessPointsInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private int maxResults;

    @JsonIgnore
    private String nextToken;

    public ListAccessPointsInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public ListAccessPointsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public ListAccessPointsInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public ListAccessPointsInput setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListAccessPointsInput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    @Override
    public String toString() {
        return "ListAccessPointsInput{" +
                "accountId='" + accountId + '\'' +
                ", bucket='" + bucket + '\'' +
                ", maxResults=" + maxResults +
                ", nextToken='" + nextToken + '\'' +
                '}';
    }

    public static ListAccessPointsInputBuilder builder() {
        return new ListAccessPointsInputBuilder();
    }

    public static final class ListAccessPointsInputBuilder {
        private String accountId;
        private String bucket;
        private int maxResults;
        private String nextToken;

        private ListAccessPointsInputBuilder() {
        }

        public ListAccessPointsInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public ListAccessPointsInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListAccessPointsInputBuilder maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public ListAccessPointsInputBuilder nextToken(String nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public ListAccessPointsInput build() {
            ListAccessPointsInput input = new ListAccessPointsInput();
            input.setAccountId(accountId);
            input.setBucket(bucket);
            input.setMaxResults(maxResults);
            input.setNextToken(nextToken);
            return input;
        }
    }
}
