package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListVectorBucketsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("vectorBuckets")
    private List<VectorBucket> vectorBuckets;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListVectorBucketsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVectorBucketsOutput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public List<VectorBucket> getVectorBuckets() {
        return vectorBuckets;
    }

    public ListVectorBucketsOutput setVectorBuckets(List<VectorBucket> vectorBuckets) {
        this.vectorBuckets = vectorBuckets;
        return this;
    }

    @Override
    public String toString() {
        return "ListVectorBucketsOutput{" +
                "requestInfo=" + requestInfo +
                ", nextToken='" + nextToken + '\'' +
                ", vectorBuckets=" + vectorBuckets +
                '}';
    }
}