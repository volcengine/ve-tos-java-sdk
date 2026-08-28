package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class PutVectorsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutVectorsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutVectorsOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}