package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class SetObjectMetaOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public SetObjectMetaOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "SetObjectMetaOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
