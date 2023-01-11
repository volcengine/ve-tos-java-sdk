package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectTaggingOutput {
    private RequestInfo requestInfo;
    private String versionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public PutObjectTaggingOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectTaggingOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
