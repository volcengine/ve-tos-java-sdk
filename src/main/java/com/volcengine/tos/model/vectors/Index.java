package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.comm.common.DataType;
import com.volcengine.tos.comm.common.DistanceMetricType;
import com.volcengine.tos.internal.model.SecondTimestampDeserializer;
import com.volcengine.tos.internal.model.SecondTimestampSerializer;
import java.util.Date;

public class Index {
    @JsonSerialize(using = SecondTimestampSerializer.class)
    @JsonDeserialize(using = SecondTimestampDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;
    
    @JsonProperty("dataType")
    private DataType dataType;
    
    @JsonProperty("dimension")
    private Integer dimension;
    
    @JsonProperty("distanceMetric")
    private DistanceMetricType distanceMetric;
    
    @JsonProperty("metadataConfiguration")
    private MetadataConfiguration metadataConfiguration;
    
    @JsonProperty("indexName")
    private String indexName;
    
    @JsonProperty("indexTrn")
    private String indexTrn;
    
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    public Date getCreationTime() {
        return creationTime;
    }

    public Index setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Index setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public int getDimension() {
        return dimension;
    }

    public Index setDimension(int dimension) {
        this.dimension = dimension;
        return this;
    }

    public DistanceMetricType getDistanceMetric() {
        return distanceMetric;
    }

    public Index setDistanceMetric(DistanceMetricType distanceMetric) {
        this.distanceMetric = distanceMetric;
        return this;
    }

    public MetadataConfiguration getMetadataConfiguration() {
        return metadataConfiguration;
    }

    public Index setMetadataConfiguration(MetadataConfiguration metadataConfiguration) {
        this.metadataConfiguration = metadataConfiguration;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public Index setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public String getIndexTrn() {
        return indexTrn;
    }

    public Index setIndexTrn(String indexTrn) {
        this.indexTrn = indexTrn;
        return this;
    }

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public Index setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    @Override
    public String toString() {
        return "Index{" +
                "creationTime=" + creationTime +
                ", dataType=" + dataType +
                ", dimension=" + dimension +
                ", distanceMetric=" + distanceMetric +
                ", metadataConfiguration=" + metadataConfiguration +
                ", indexName='" + indexName + '\'' +
                ", indexTrn='" + indexTrn + '\'' +
                ", vectorBucketName='" + vectorBucketName + '\'' +
                '}';
    }
}