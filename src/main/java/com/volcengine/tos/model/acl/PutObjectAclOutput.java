package com.volcengine.tos.model.acl;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectAclOutput {

    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectAclOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }
}
