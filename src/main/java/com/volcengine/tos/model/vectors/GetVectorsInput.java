package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class GetVectorsInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("keys")
    private List<String> keys;

    @JsonProperty("returnMetadata")
    private Boolean returnMetadata;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public GetVectorsInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public GetVectorsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public GetVectorsInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public List<String> getKeys() {
        return keys;
    }

    public GetVectorsInput setKeys(List<String> keys) {
        this.keys = keys;
        return this;
    }

    public Boolean getReturnMetadata() {
        return returnMetadata;
    }

    public GetVectorsInput setReturnMetadata(Boolean returnMetadata) {
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
        private List<String> keys;
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

        public Builder keys(List<String> keys) {
            this.keys = keys;
            return this;
        }

        public Builder returnMetadata(Boolean returnMetadata) {
            this.returnMetadata = returnMetadata;
            return this;
        }

        public GetVectorsInput build() {
            GetVectorsInput input = new GetVectorsInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setKeys(keys);
            input.setReturnMetadata(returnMetadata);
            return input;
        }
    }

    @Override
    public String toString() {
        return "GetVectorsInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", keys=" + (keys != null ? keys.size() + " keys" : "null") +
                ", returnMetadata=" + returnMetadata +
                '}';
    }
}
