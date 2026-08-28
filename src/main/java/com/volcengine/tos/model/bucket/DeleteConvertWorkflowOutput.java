package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class DeleteConvertWorkflowOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteConvertWorkflowOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteConvertWorkflowOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}