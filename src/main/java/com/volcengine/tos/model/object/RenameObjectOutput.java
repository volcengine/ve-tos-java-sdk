package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class RenameObjectOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public RenameObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "RenameObjectOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
