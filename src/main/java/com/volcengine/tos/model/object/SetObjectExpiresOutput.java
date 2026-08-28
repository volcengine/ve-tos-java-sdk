package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class SetObjectExpiresOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public SetObjectExpiresOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "SetObjectExpiresOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
