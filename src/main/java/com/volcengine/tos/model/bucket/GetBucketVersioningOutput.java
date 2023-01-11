package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.VersioningStatusType;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketVersioningOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Status")
    private VersioningStatusType status;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketVersioningOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public VersioningStatusType getStatus() {
        return status;
    }

    public GetBucketVersioningOutput setStatus(VersioningStatusType status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketVersioningOutput{" +
                "requestInfo=" + requestInfo +
                ", status=" + status +
                '}';
    }
}
