package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class SetObjectTimeOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public SetObjectTimeOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "SetObjectTimeOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
