package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetVectorsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("vectors")
    private List<VectorMeta> vectors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetVectorsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<VectorMeta> getVectors() {
        return vectors;
    }

    public GetVectorsOutput setVectors(List<VectorMeta> vectors) {
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