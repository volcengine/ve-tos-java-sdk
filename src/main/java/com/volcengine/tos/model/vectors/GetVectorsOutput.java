package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetVectorsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("vectors")
    private List<Vector> vectors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetVectorsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public GetVectorsOutput setVectors(List<Vector> vectors) {
        this.vectors = vectors;
        return this;
    }

    @Override
    public String toString() {
        return "GetVectorsOutput{" +
                "requestInfo=" + requestInfo +
                ", vectors=" + (vectors != null ? vectors.size() + " vectors" : "null") +
                '}';
    }
}