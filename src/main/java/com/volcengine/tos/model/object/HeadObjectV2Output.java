package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.ReplicationStatusType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.model.RequestInfo;

import java.util.Date;
import java.util.Map;

public class HeadObjectV2Output {
    private GetObjectBasicOutput headObjectBasicOutput = new GetObjectBasicOutput();

    private long symlinkTargetSize;

    public HeadObjectV2Output() {
    }

    @Deprecated
    public HeadObjectV2Output(GetObjectBasicOutput getObjectBasicOutput) {
        ParamsChecker.ensureNotNull(getObjectBasicOutput, "GetObjectBasicOutput");
        this.headObjectBasicOutput = getObjectBasicOutput;
    }

    @Deprecated
    public GetObjectBasicOutput getHeadObjectBasicOutput() {
        return headObjectBasicOutput;
    }

    @Deprecated
    public HeadObjectV2Output setHeadObjectBasicOutput(GetObjectBasicOutput headObjectBasicOutput) {
        this.headObjectBasicOutput = headObjectBasicOutput;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return headObjectBasicOutput.getRequestInfo();
    }

    public HeadObjectV2Output setRequestInfo(RequestInfo info) {
        headObjectBasicOutput.setRequestInfo(info);
        return this;
    }

    public String getContentRange() {
        return headObjectBasicOutput.getContentRange();
    }

    public String getEtag() {
        return headObjectBasicOutput.getEtag();
    }

    public String getLastModified() {
        return headObjectBasicOutput.getLastModified();
    }

    public Date getLastModifiedInDate() {
        return headObjectBasicOutput.getLastModifiedInDate();
    }

    public boolean isDeleteMarker() {
        return headObjectBasicOutput.isDeleteMarker();
    }

    public String getSsecAlgorithm() {
        return headObjectBasicOutput.getSsecAlgorithm();
    }

    public String getSsecKeyMD5() {
        return headObjectBasicOutput.getSsecKeyMD5();
    }

    public String getVersionID() {
        return headObjectBasicOutput.getVersionID();
    }

    public String getWebsiteRedirectLocation() {
        return headObjectBasicOutput.getWebsiteRedirectLocation();
    }

    public String getObjectType() {
        return headObjectBasicOutput.getObjectType();
    }

    public String getHashCrc64ecma() {
        return headObjectBasicOutput.getHashCrc64ecma();
    }

    public StorageClassType getStorageClass() {
        return headObjectBasicOutput.getStorageClass();
    }

    public Map<String, String> getCustomMetadata() {
        return headObjectBasicOutput.getCustomMetadata();
    }

    public long getContentLength() {
        return headObjectBasicOutput.getContentLength();
    }

    public String getCacheControl() {
        return headObjectBasicOutput.getCacheControl();
    }

    public String getContentDisposition() {
        return headObjectBasicOutput.getContentDisposition();
    }

    public String getContentEncoding() {
        return headObjectBasicOutput.getContentEncoding();
    }

    public String getContentLanguage() {
        return headObjectBasicOutput.getContentLanguage();
    }

    public String getContentType() {
        return headObjectBasicOutput.getContentType();
    }

    public String getExpires() {
        return headObjectBasicOutput.getExpires();
    }

    public Date getExpiresInDate() {
        return headObjectBasicOutput.getExpiresInDate();
    }

    public String getContentMD5() {
        return headObjectBasicOutput.getContentMD5();
    }

    public RestoreInfo getRestoreInfo() {
        return headObjectBasicOutput.getRestoreInfo();
    }

    public ReplicationStatusType getReplicationStatus() {
        return headObjectBasicOutput.getReplicationStatus();
    }

    public long getSymlinkTargetSize() {
        return symlinkTargetSize;
    }

    public HeadObjectV2Output setSymlinkTargetSize(long symlinkTargetSize) {
        this.symlinkTargetSize = symlinkTargetSize;
        return this;
    }

    public boolean isDirectory() {
        return headObjectBasicOutput.isDirectory();
    }

    public int getTaggingCount() {
        return headObjectBasicOutput.getTaggingCount();
    }

    @Override
    public String toString() {
        return "HeadObjectOutputV2{" +
                "requestInfo=" + getRequestInfo() +
                ", contentRange='" + getContentRange() + '\'' +
                ", etag='" + getEtag() + '\'' +
                ", lastModified=" + getLastModified() +
                ", deleteMarker=" + isDeleteMarker() +
                ", ssecAlgorithm='" + getSsecAlgorithm() + '\'' +
                ", ssecKeyMD5='" + getSsecKeyMD5() + '\'' +
                ", versionID='" + getVersionID() + '\'' +
                ", websiteRedirectLocation='" + getWebsiteRedirectLocation() + '\'' +
                ", objectType='" + getObjectType() + '\'' +
                ", hashCrc64ecma=" + getHashCrc64ecma() +
                ", storageClass=" + getStorageClass() +
                ", metadata=" + getCustomMetadata() +
                ", cacheControl='" + getCacheControl() + '\'' +
                ", contentDisposition='" + getContentDisposition() + '\'' +
                ", contentEncoding='" + getContentEncoding() + '\'' +
                ", contentLanguage='" + getContentLanguage() + '\'' +
                ", contentType='" + getContentType() + '\'' +
                ", expires=" + getExpires() + '\'' +
                ", symlinkTargetSize=" + symlinkTargetSize +
                ", isDirectory=" + isDirectory() + '\'' +
                ", taggingCount=" + getTaggingCount() + '\'' +
                '}';
    }
}
