package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class GetIndexOutput {
    @JsonProperty("index")
    private Index index;
    
    @JsonIgnore
    private RequestInfo requestInfo;

    public Index getIndex() {
        return index;
    }

    public GetIndexOutput setIndex(Index index) {
        this.index = index;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetIndexOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "GetIndexOutput{" +
                "index=" + index +
                ", requestInfo=" + requestInfo +
                '}';
    }
}