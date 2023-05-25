package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketRenameOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("RenameEnable")
    private boolean renameEnable;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketRenameOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public boolean isRenameEnable() {
        return renameEnable;
    }

    public GetBucketRenameOutput setRenameEnable(boolean renameEnable) {
        this.renameEnable = renameEnable;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketRenameOutput{" +
                "requestInfo=" + requestInfo +
                ", renameEnable=" + renameEnable +
                '}';
    }
}
