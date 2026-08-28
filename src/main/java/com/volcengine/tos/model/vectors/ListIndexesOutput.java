package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListIndexesOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("nextToken")
    private String nextToken;
    
    @JsonProperty("indexes")
    private List<IndexSummary> indexes;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListIndexesOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListIndexesOutput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public List<IndexSummary> getIndexes() {
        return indexes;
    }

    public ListIndexesOutput setIndexes(List<IndexSummary> indexes) {
        this.indexes = indexes;
        return this;
    }

    @Override
    public String toString() {
        return "ListIndexesOutput{" +
                "requestInfo=" + requestInfo +
                ", nextToken='" + nextToken + '\'' +
                ", indexes=" + indexes +
                '}';
    }
}