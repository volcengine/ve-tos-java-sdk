package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketRealTimeLogOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("RealTimeLogConfiguration")
    private RealTimeLogConfiguration configuration;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketRealTimeLogOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public RealTimeLogConfiguration getConfiguration() {
        return configuration;
    }

    public GetBucketRealTimeLogOutput setConfiguration(RealTimeLogConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketRealTimeLogOutput{" +
                "requestInfo=" + requestInfo +
                ", configuration=" + configuration +
                '}';
    }
}
