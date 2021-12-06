package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class DeleteObjectOutput {
    private RequestInfo requestInfo;
    private boolean deleteMarker;
    private String versionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public boolean isDeleteMarker() {
        return deleteMarker;
    }

    public DeleteObjectOutput setDeleteMarker(boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", deleteMarker=" + deleteMarker +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
