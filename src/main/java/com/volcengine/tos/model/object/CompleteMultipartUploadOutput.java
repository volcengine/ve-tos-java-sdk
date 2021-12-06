package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class CompleteMultipartUploadOutput {
    private RequestInfo requestInfo;
    private String versionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CompleteMultipartUploadOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public CompleteMultipartUploadOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
