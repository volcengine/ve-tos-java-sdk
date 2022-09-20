package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class ListBucketsV2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Buckets")
    private List<ListedBucket> buckets;
    @JsonProperty("Owner")
    private Owner owner;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBucketsV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ListedBucket> getBuckets() {
        return buckets;
    }

    public ListBucketsV2Output setBuckets(List<ListedBucket> buckets) {
        this.buckets = buckets;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListBucketsV2Output setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "ListBucketsV2Output{" +
                "requestInfo=" + requestInfo +
                ", buckets=" + buckets +
                ", owner=" + owner +
                '}';
    }
}
