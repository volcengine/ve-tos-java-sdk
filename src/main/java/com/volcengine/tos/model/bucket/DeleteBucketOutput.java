package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;
public class DeleteBucketOutput implements Serializable {
    private RequestInfo requestInfo;

    public DeleteBucketOutput(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteBucketOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }
}
