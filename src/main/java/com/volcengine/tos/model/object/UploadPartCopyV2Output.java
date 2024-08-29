package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

import java.util.Date;

public class UploadPartCopyV2Output {
    private RequestInfo requestInfo;
    private int partNumber;
    private String etag;
    private Date lastModified;
    private String copySourceVersionID;
    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String serverSideEncryption;
    private String serverSideEncryptionKeyID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public UploadPartCopyV2Output requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartCopyV2Output partNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadPartCopyV2Output etag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public UploadPartCopyV2Output setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public UploadPartCopyV2Output setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public UploadPartCopyV2Output setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
        return this;
    }

    public String getServerSideEncryptionKeyID() {
        return serverSideEncryptionKeyID;
    }

    public UploadPartCopyV2Output setServerSideEncryptionKeyID(String serverSideEncryptionKeyID) {
        this.serverSideEncryptionKeyID = serverSideEncryptionKeyID;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public UploadPartCopyV2Output lastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getCopySourceVersionID() {
        return copySourceVersionID;
    }

    public UploadPartCopyV2Output copySourceVersionID(String copySourceVersionID) {
        this.copySourceVersionID = copySourceVersionID;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartCopyV2Output{" +
                "requestInfo=" + requestInfo +
                ", partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", serverSideEncryptionKeyID='" + serverSideEncryptionKeyID + '\'' +
                ", lastModified=" + lastModified +
                ", copySourceVersionID='" + copySourceVersionID + '\'' +
                '}';
    }
}
