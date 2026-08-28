package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class QueryVectorsInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonIgnore
    private String accountId;

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("returnDistance")
    private Boolean returnDistance;

    @JsonProperty("returnMetadata")
    private Boolean returnMetadata;

    @JsonProperty("topK")
    private int topK;

    @JsonProperty("queryVector")
    private VectorData queryVector;

    @JsonProperty("filter")
    private Map<String, Object> filter;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public QueryVectorsInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public QueryVectorsInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public QueryVectorsInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public Boolean getReturnDistance() {
        return returnDistance;
    }

    public QueryVectorsInput setReturnDistance(Boolean returnDistance) {
        this.returnDistance = returnDistance;
        return this;
    }

    public Boolean getReturnMetadata() {
        return returnMetadata;
    }

    public QueryVectorsInput setReturnMetadata(Boolean returnMetadata) {
        this.returnMetadata = returnMetadata;
        return this;
    }

    public int getTopK() {
        return topK;
    }

    public QueryVectorsInput setTopK(int topK) {
        this.topK = topK;
        return this;
    }

    public VectorData getQueryVector() {
        return queryVector;
    }

    public QueryVectorsInput setQueryVector(VectorData queryVector) {
        this.queryVector = queryVector;
        return this;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public QueryVectorsInput setFilter(Map<String, Object> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public String toString() {
        return "QueryVectorsInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", returnDistance=" + returnDistance +
                ", returnMetadata=" + returnMetadata +
                ", topK=" + topK +
                ", queryVector=" + queryVector +
                ", filter=" + filter +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;
        private Boolean returnDistance;
        private Boolean returnMetadata;
        private int topK;
        private VectorData queryVector;
        private Map<String, Object> filter;

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

        public Builder returnDistance(Boolean returnDistance) {
            this.returnDistance = returnDistance;
            return this;
        }

        public Builder returnMetadata(Boolean returnMetadata) {
            this.returnMetadata = returnMetadata;
            return this;
        }

        public Builder topK(int topK) {
            this.topK = topK;
            return this;
        }

        public Builder queryVector(VectorData queryVector) {
            this.queryVector = queryVector;
            return this;
        }

        public Builder filter(Map<String, Object> filter) {
            this.filter = filter;
            return this;
        }

        public QueryVectorsInput build() {
            QueryVectorsInput input = new QueryVectorsInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setReturnDistance(returnDistance);
            input.setReturnMetadata(returnMetadata);
            input.setTopK(topK);
            input.setQueryVector(queryVector);
            input.setFilter(filter);
            return input;
        }
    }
}