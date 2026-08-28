package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class DeleteVectorsInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("keys")
    private List<String> keys;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public DeleteVectorsInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public DeleteVectorsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public DeleteVectorsInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public List<String> getKeys() {
        return keys;
    }

    public DeleteVectorsInput setKeys(List<String> keys) {
        this.keys = keys;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;
        private List<String> keys;

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

        public Builder keys(List<String> keys) {
            this.keys = keys;
            return this;
        }

        public DeleteVectorsInput build() {
            DeleteVectorsInput input = new DeleteVectorsInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setKeys(keys);
            return input;
        }
    }

    @Override
    public String toString() {
        return "DeleteVectorsInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", keys=" + (keys != null ? keys.size() + " keys" : "null") +
                '}';
    }
}