package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListAccessPointsOutput {

    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("IsTruncated")
    private boolean isTruncated;

    @JsonProperty("NextToken")
    private String nextToken;

    @JsonProperty("AccessPoints")
    private List<AccessPoint> accessPoints;

    public ListAccessPointsOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListAccessPointsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListAccessPointsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListAccessPointsOutput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public ListAccessPointsOutput setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
        return this;
    }

    @Override
    public String toString() {
        return "ListAccessPointsOutput{" +
                "requestInfo=" + requestInfo +
                ", isTruncated=" + isTruncated +
                ", nextToken='" + nextToken + '\'' +
                ", accessPoints=" + accessPoints +
                '}';
    }
}
