package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListBindAccessPointForAcceleratorOutput {

    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("AccessPoints")
    private List<AccessPointSummary> accessPoints;

    public ListBindAccessPointForAcceleratorOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBindAccessPointForAcceleratorOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<AccessPointSummary> getAccessPoints() {
        return accessPoints;
    }

    public ListBindAccessPointForAcceleratorOutput setAccessPoints(List<AccessPointSummary> accessPoints) {
        this.accessPoints = accessPoints;
        return this;
    }

    @Override
    public String toString() {
        return "ListBindAccessPointForAcceleratorOutput{" +
                "requestInfo=" + requestInfo +
                ", accessPoints=" + accessPoints +
                '}';
    }
}
