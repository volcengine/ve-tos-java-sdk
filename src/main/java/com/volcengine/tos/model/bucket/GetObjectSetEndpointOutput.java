package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetObjectSetEndpointOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    private List<ObjectSetEndpoint> endpoints;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetEndpointOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ObjectSetEndpoint> getEndpoints() {
        return endpoints;
    }

    public GetObjectSetEndpointOutput setEndpoints(List<ObjectSetEndpoint> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetEndpointOutput{" +
                "requestInfo=" + requestInfo +
                ", endpoints=" + endpoints +
                '}';
    }
}
