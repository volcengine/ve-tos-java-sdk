package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class ListBucketsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Buckets")
    private ListedBucket[] buckets;
    @JsonProperty("Owner")
    private ListedOwner owner;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBucketsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public ListedBucket[] getBuckets() {
        return buckets;
    }

    public ListedOwner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "ListBucketsOutput{" +
                "requestInfo=" + requestInfo +
                ", buckets=" + Arrays.toString(buckets) +
                ", owner=" + owner +
                '}';
    }
}
