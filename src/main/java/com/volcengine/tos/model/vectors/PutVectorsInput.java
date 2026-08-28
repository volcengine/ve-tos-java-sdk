package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutVectorsInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("vectors")
    private List<Vector> vectors;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public PutVectorsInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public PutVectorsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public PutVectorsInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public PutVectorsInput setVectors(List<Vector> vectors) {
        this.vectors = vectors;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;
        private List<Vector> vectors;

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

        public Builder vectors(List<Vector> vectors) {
            this.vectors = vectors;
            return this;
        }

        public PutVectorsInput build() {
            PutVectorsInput input = new PutVectorsInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setVectors(vectors);
            return input;
        }
    }

    @Override
    public String toString() {
        return "PutVectorsInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", vectors=" + (vectors != null ? vectors.size() + " vectors" : "null") +
                '}';
    }
}