package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

import java.util.Date;

public class GetSymlinkOutput {
    private RequestInfo requestInfo;
    private String versionID;
    private Date lastModified;
    private String symlinkTargetKey;
    private String symlinkTargetBucket;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetSymlinkOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetSymlinkOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public GetSymlinkOutput setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getSymlinkTargetKey() {
        return symlinkTargetKey;
    }

    public GetSymlinkOutput setSymlinkTargetKey(String symlinkTargetKey) {
        this.symlinkTargetKey = symlinkTargetKey;
        return this;
    }

    public String getSymlinkTargetBucket() {
        return symlinkTargetBucket;
    }

    public GetSymlinkOutput setSymlinkTargetBucket(String symlinkTargetBucket) {
        this.symlinkTargetBucket = symlinkTargetBucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetSymlinkOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", lastModified=" + lastModified +
                ", symlinkTargetKey='" + symlinkTargetKey + '\'' +
                ", symlinkTargetBucket='" + symlinkTargetBucket + '\'' +
                '}';
    }
}
