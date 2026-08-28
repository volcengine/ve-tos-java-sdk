package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketAccessMonitorOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Status")
    private StatusType status;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketAccessMonitorOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public GetBucketAccessMonitorOutput setStatus(StatusType status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketAccessMonitorOutput{" +
                "requestInfo=" + requestInfo +
                ", status=" + status +
                '}';
    }
}