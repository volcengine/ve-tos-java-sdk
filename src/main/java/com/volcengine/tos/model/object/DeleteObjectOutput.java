package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class DeleteObjectOutput {
    private RequestInfo requestInfo;
    private boolean deleteMarker;
    private String versionID;
    private String trashPath;

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

    public String getTrashPath() {
        return trashPath;
    }

    public DeleteObjectOutput setTrashPath(String trashPath) {
        this.trashPath = trashPath;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", deleteMarker=" + deleteMarker +
                ", versionID='" + versionID + '\'' +
                ", trashPath='" + trashPath + '\'' +
                '}';
    }
}
