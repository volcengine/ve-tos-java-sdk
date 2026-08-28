package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListObjectSetsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("IsTruncated")
    private Boolean isTruncated;

    @JsonProperty("NextMarker")
    private String nextMarker;

    @JsonProperty("ObjectSets")
    private List<ObjectSetInfo> objectSets;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectSetsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public Boolean getIsTruncated() {
        return isTruncated;
    }

    public ListObjectSetsOutput setIsTruncated(Boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public ListObjectSetsOutput setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
        return this;
    }

    public List<ObjectSetInfo> getObjectSets() {
        return objectSets;
    }

    public ListObjectSetsOutput setObjectSets(List<ObjectSetInfo> objectSets) {
        this.objectSets = objectSets;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectSetsOutput{" +
                "requestInfo=" + requestInfo +
                ", isTruncated=" + isTruncated +
                ", nextMarker='" + nextMarker + '\'' +
                ", objectSets=" + objectSets +
                '}';
    }
}
