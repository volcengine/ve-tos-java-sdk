package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class DeleteObjectTaggingOutput {
    private RequestInfo requestInfo;
    private String versionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteObjectTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteObjectTaggingOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectTaggingOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
