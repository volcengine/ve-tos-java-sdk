package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListVectorsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("vectors")
    private List<Vector> vectors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListVectorsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVectorsOutput setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public ListVectorsOutput setVectors(List<Vector> vectors) {
        this.vectors = vectors;
        return this;
    }

    @Override
    public String toString() {
        return "ListVectorsOutput{" +
                "requestInfo=" + requestInfo +
                ", nextToken='" + nextToken + '\'' +
                ", vectors=" + (vectors != null ? vectors.size() + " vectors" : "null") +
                '}';
    }
}