package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.Date;

public class CopyObjectV2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("ETag")
    private String etag;
    private String versionID;
    private String copySourceVersionID;
    @JsonProperty("LastModified")
    private Date lastModified;
    private String hashCrc64ecma;

    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String serverSideEncryption;
    private String serverSideEncryptionKeyID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CopyObjectV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public CopyObjectV2Output setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getSourceVersionID() {
        return copySourceVersionID;
    }

    public CopyObjectV2Output setSourceVersionID(String sourceVersionID) {
        this.copySourceVersionID = sourceVersionID;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public CopyObjectV2Output setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public CopyObjectV2Output setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public CopyObjectV2Output setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public CopyObjectV2Output setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public CopyObjectV2Output setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public CopyObjectV2Output setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
        return this;
    }

    public String getServerSideEncryptionKeyID() {
        return serverSideEncryptionKeyID;
    }

    public CopyObjectV2Output setServerSideEncryptionKeyID(String serverSideEncryptionKeyID) {
        this.serverSideEncryptionKeyID = serverSideEncryptionKeyID;
        return this;
    }

    @Override
    public String toString() {
        return "CopyObjectV2Output{" +
                "requestInfo=" + requestInfo +
                ", etag='" + etag + '\'' +
                ", versionID='" + versionID + '\'' +
                ", copySourceVersionID='" + copySourceVersionID + '\'' +
                ", lastModified=" + lastModified +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", serverSideEncryptionKeyID='" + serverSideEncryptionKeyID + '\'' +
                '}';
    }
}
