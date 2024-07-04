package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListBucketInventoryOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("InventoryConfigurations")
    private List<BucketInventoryConfiguration> configurations;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("NextContinuationToken")
    private String nextContinuationToken;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBucketInventoryOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<BucketInventoryConfiguration> getConfigurations() {
        return configurations;
    }

    public ListBucketInventoryOutput setConfigurations(List<BucketInventoryConfiguration> configurations) {
        this.configurations = configurations;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListBucketInventoryOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getNextContinuationToken() {
        return nextContinuationToken;
    }

    public ListBucketInventoryOutput setNextContinuationToken(String nextContinuationToken) {
        this.nextContinuationToken = nextContinuationToken;
        return this;
    }

    @Override
    public String toString() {
        return "ListBucketInventoryOutput{" +
                "requestInfo=" + requestInfo +
                ", configurations=" + configurations +
                ", isTruncated=" + isTruncated +
                ", nextContinuationToken='" + nextContinuationToken + '\'' +
                '}';
    }
}
