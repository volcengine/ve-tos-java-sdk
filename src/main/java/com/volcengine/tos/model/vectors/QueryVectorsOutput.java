package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class QueryVectorsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("vectors")
    private List<DistanceVector> vectors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public QueryVectorsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<DistanceVector> getVectors() {
        return vectors;
    }

    public QueryVectorsOutput setVectors(List<DistanceVector> vectors) {
        this.vectors = vectors;
        return this;
    }

    @Override
    public String toString() {
        return "QueryVectorsOutput{" +
                "requestInfo=" + requestInfo +
                ", vectors=" + vectors +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RequestInfo requestInfo;
        private List<DistanceVector> vectors;

        public Builder requestInfo(RequestInfo requestInfo) {
            this.requestInfo = requestInfo;
            return this;
        }

        public Builder vectors(List<DistanceVector> vectors) {
            this.vectors = vectors;
            return this;
        }

        public QueryVectorsOutput build() {
            QueryVectorsOutput output = new QueryVectorsOutput();
            output.setRequestInfo(requestInfo);
            output.setVectors(vectors);
            return output;
        }
    }
}