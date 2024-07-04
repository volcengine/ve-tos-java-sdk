package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class PutSymlinkOutput {
    private RequestInfo requestInfo;
    private String versionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutSymlinkOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public PutSymlinkOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "PutSymlinkOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
