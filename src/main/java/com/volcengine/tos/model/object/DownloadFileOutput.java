package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.RequestInfo;

import java.util.Date;
import java.util.Map;

public class DownloadFileOutput {
    private HeadObjectV2Output output = new HeadObjectV2Output();

    @Deprecated
    public HeadObjectV2Output getOutput() {
        return output;
    }

    public DownloadFileOutput setOutput(HeadObjectV2Output output) {
        this.output = output;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return output.getRequestInfo();
    }

    public String getContentRange() {
        return output.getContentRange();
    }

    public String getEtag() {
        return output.getEtag();
    }

    public String getLastModified() {
        return output.getLastModified();
    }

    public Date getLastModifiedInDate() {
        return output.getLastModifiedInDate();
    }

    public boolean isDeleteMarker() {
        return output.isDeleteMarker();
    }

    public String getSsecAlgorithm() {
        return output.getSsecAlgorithm();
    }

    public String getSsecKeyMD5() {
        return output.getSsecKeyMD5();
    }

    public String getVersionID() {
        return output.getVersionID();
    }

    public String getWebsiteRedirectLocation() {
        return output.getWebsiteRedirectLocation();
    }

    public String getObjectType() {
        return output.getObjectType();
    }

    public String getHashCrc64ecma() {
        return output.getHashCrc64ecma();
    }

    public StorageClassType getStorageClass() {
        return output.getStorageClass();
    }

    public Map<String, String> getCustomMetadata() {
        return output.getCustomMetadata();
    }

    public long getContentLength() {
        return output.getContentLength();
    }

    public String getCacheControl() {
        return output.getCacheControl();
    }

    public String getContentDisposition() {
        return output.getContentDisposition();
    }

    public String getContentEncoding() {
        return output.getContentEncoding();
    }

    public String getContentLanguage() {
        return output.getContentLanguage();
    }

    public String getContentType() {
        return output.getContentType();
    }

    public String getExpires() {
        return output.getExpires();
    }

    public Date getExpiresInDate() {
        return output.getExpiresInDate();
    }

    public String getContentMD5() {
        return output.getContentMD5();
    }

    @Override
    public String toString() {
        return "DownloadFileOutput{" +
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
                '}';
    }
}
