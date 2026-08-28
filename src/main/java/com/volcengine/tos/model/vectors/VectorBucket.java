package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.SecondTimestampDeserializer;
import com.volcengine.tos.internal.model.SecondTimestampSerializer;

import java.util.Date;

public class VectorBucket {
    @JsonSerialize(using = SecondTimestampSerializer.class)
    @JsonDeserialize(using = SecondTimestampDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;

    @JsonProperty("vectorBucketTrn")
    private String vectorBucketTrn;

    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    @JsonProperty("projectName")
    private String projectName;

    public Date getCreationTime() {
        return creationTime;
    }

    public VectorBucket setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public String getVectorBucketTrn() {
        return vectorBucketTrn;
    }

    public VectorBucket setVectorBucketTrn(String vectorBucketTrn) {
        this.vectorBucketTrn = vectorBucketTrn;
        return this;
    }

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public VectorBucket setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public VectorBucket setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
}