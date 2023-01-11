package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.model.RequestInfo;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class GetObjectV2Output implements Closeable {
    private GetObjectBasicOutput getObjectBasicOutput;
    private transient InputStream content;

    public GetObjectV2Output(GetObjectBasicOutput getObjectBasicOutput, InputStream content) {
        ParamsChecker.ensureNotNull(getObjectBasicOutput, "GetObjectBasicOutput");
        this.getObjectBasicOutput = getObjectBasicOutput;
        this.content = content;
    }

    @Deprecated
    public GetObjectBasicOutput getGetObjectBasicOutput() {
        return getObjectBasicOutput;
    }

    public InputStream getContent() {
        return content;
    }

    @Deprecated
    public GetObjectV2Output setGetObjectBasicOutput(GetObjectBasicOutput getObjectBasicOutput) {
        this.getObjectBasicOutput = getObjectBasicOutput;
        return this;
    }

    public GetObjectV2Output setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return getObjectBasicOutput.getRequestInfo();
    }

    public GetObjectV2Output setRequestInfo(RequestInfo info) {
        getObjectBasicOutput.setRequestInfo(info);
        return this;
    }

    public String getContentRange() {
        return getObjectBasicOutput.getContentRange();
    }

    public String getEtag() {
        return getObjectBasicOutput.getEtag();
    }

    public String getLastModified() {
        return getObjectBasicOutput.getLastModified();
    }

    public Date getLastModifiedInDate() {
        return getObjectBasicOutput.getLastModifiedInDate();
    }

    public boolean isDeleteMarker() {
        return getObjectBasicOutput.isDeleteMarker();
    }

    public String getSsecAlgorithm() {
        return getObjectBasicOutput.getSsecAlgorithm();
    }

    public String getSsecKeyMD5() {
        return getObjectBasicOutput.getSsecKeyMD5();
    }

    public String getVersionID() {
        return getObjectBasicOutput.getVersionID();
    }

    public String getWebsiteRedirectLocation() {
        return getObjectBasicOutput.getWebsiteRedirectLocation();
    }

    public String getObjectType() {
        return getObjectBasicOutput.getObjectType();
    }

    public String getHashCrc64ecma() {
        return getObjectBasicOutput.getHashCrc64ecma();
    }

    public StorageClassType getStorageClass() {
        return getObjectBasicOutput.getStorageClass();
    }

    public Map<String, String> getCustomMetadata() {
        return getObjectBasicOutput.getCustomMetadata();
    }

    public long getContentLength() {
        return getObjectBasicOutput.getContentLength();
    }

    public String getCacheControl() {
        return getObjectBasicOutput.getCacheControl();
    }

    public String getContentDisposition() {
        return getObjectBasicOutput.getContentDisposition();
    }

    public String getContentEncoding() {
        return getObjectBasicOutput.getContentEncoding();
    }

    public String getContentLanguage() {
        return getObjectBasicOutput.getContentLanguage();
    }

    public String getContentType() {
        return getObjectBasicOutput.getContentType();
    }

    public String getExpires() {
        return getObjectBasicOutput.getExpires();
    }

    public Date getExpiresInDate() {
        return getObjectBasicOutput.getExpiresInDate();
    }

    public String getContentMD5() {
        return getObjectBasicOutput.getContentMD5();
    }

    @Override
    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
        }
    }

    @Override
    public String toString() {
        return "GetObjectV2Output{" +
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
