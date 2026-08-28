package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.DataType;
import com.volcengine.tos.comm.common.DistanceMetricType;
import com.volcengine.tos.model.GenericInput;

public class CreateIndexInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;
    
    @JsonIgnore
    private String accountId;
    
    @JsonProperty("indexName")
    private String indexName;
    
    @JsonProperty("dataType")
    private DataType dataType;
    
    @JsonProperty("dimension")
    private Integer dimension;
    
    @JsonProperty("distanceMetric")
    private DistanceMetricType distanceMetric;
    
    @JsonProperty("metadataConfiguration")
    private MetadataConfiguration metadataConfiguration;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public CreateIndexInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public CreateIndexInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public CreateIndexInput setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public DataType getDataType() {
        return dataType;
    }

    public CreateIndexInput setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Integer getDimension() {
        return dimension;
    }

    public CreateIndexInput setDimension(Integer dimension) {
        this.dimension = dimension;
        return this;
    }

    public DistanceMetricType getDistanceMetric() {
        return distanceMetric;
    }

    public CreateIndexInput setDistanceMetric(DistanceMetricType distanceMetric) {
        this.distanceMetric = distanceMetric;
        return this;
    }

    public MetadataConfiguration getMetadataConfiguration() {
        return metadataConfiguration;
    }

    public CreateIndexInput setMetadataConfiguration(MetadataConfiguration metadataConfiguration) {
        this.metadataConfiguration = metadataConfiguration;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;
        private String accountId;
        private String indexName;
        private DataType dataType;
        private Integer dimension;
        private DistanceMetricType distanceMetric;
        private MetadataConfiguration metadataConfiguration;

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

        public Builder dataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder distanceMetric(DistanceMetricType distanceMetric) {
            this.distanceMetric = distanceMetric;
            return this;
        }

        public Builder metadataConfiguration(MetadataConfiguration metadataConfiguration) {
            this.metadataConfiguration = metadataConfiguration;
            return this;
        }

        public CreateIndexInput build() {
            CreateIndexInput input = new CreateIndexInput();
            input.setVectorBucketName(vectorBucketName);
            input.setAccountId(accountId);
            input.setIndexName(indexName);
            input.setDataType(dataType);
            input.setDimension(dimension);
            input.setDistanceMetric(distanceMetric);
            input.setMetadataConfiguration(metadataConfiguration);
            return input;
        }
    }

    @Override
    public String toString() {
        return "CreateIndexInput{" +
                "vectorBucketName='" + vectorBucketName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", indexName='" + indexName + '\'' +
                ", dataType=" + dataType +
                ", dimension=" + dimension +
                ", distanceMetric=" + distanceMetric +
                ", metadataConfiguration=" + metadataConfiguration +
                '}';
    }
}