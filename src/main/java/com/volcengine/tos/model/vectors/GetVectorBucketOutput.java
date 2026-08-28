package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetVectorBucketOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("vectorBucket")
    private VectorBucket vectorBucket;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetVectorBucketOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public VectorBucket getVectorBucket() {
        return vectorBucket;
    }

    public GetVectorBucketOutput setVectorBucket(VectorBucket vectorBucket) {
        this.vectorBucket = vectorBucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetVectorBucketOutput{" +
                "requestInfo=" + requestInfo +
                ", vectorBucket=" + vectorBucket +
                '}';
    }
}