package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class DeleteAccessPointOutput {

    @JsonIgnore
    private RequestInfo requestInfo;

    public DeleteAccessPointOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteAccessPointOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteAccessPointOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
