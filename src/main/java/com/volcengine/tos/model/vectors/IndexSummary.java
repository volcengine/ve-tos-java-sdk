package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.SecondTimestampDeserializer;
import com.volcengine.tos.internal.model.SecondTimestampSerializer;
import java.util.Date;

public class IndexSummary {
    @JsonSerialize(using = SecondTimestampSerializer.class)
    @JsonDeserialize(using = SecondTimestampDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;
    
    @JsonProperty("indexName")
    private String indexName;
    
    @JsonProperty("indexTrn")
    private String indexTrn;
    
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    public Date getCreationTime() {
        return creationTime;
    }

    public IndexSummary setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public IndexSummary setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public String getIndexTrn() {
        return indexTrn;
    }

    public IndexSummary setIndexTrn(String indexTrn) {
        this.indexTrn = indexTrn;
        return this;
    }

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public IndexSummary setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    @Override
    public String toString() {
        return "IndexSummary{" +
                "creationTime=" + creationTime +
                ", indexName='" + indexName + '\'' +
                ", indexTrn='" + indexTrn + '\'' +
                ", vectorBucketName='" + vectorBucketName + '\'' +
                '}';
    }
}