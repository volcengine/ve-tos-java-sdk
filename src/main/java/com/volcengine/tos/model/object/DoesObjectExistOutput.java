package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class DoesObjectExistOutput {
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DoesObjectExistOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DoesObjectExistOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}
