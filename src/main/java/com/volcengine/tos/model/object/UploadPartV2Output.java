package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class UploadPartV2Output {
    private RequestInfo requestInfo;
    private int partNumber;
    private String etag;
    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String serverSideEncryption;
    private String serverSideEncryptionKeyID;
    private String hashCrc64ecma;

    public UploadPartV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public UploadPartV2Output setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public UploadPartV2Output setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public UploadPartV2Output setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public UploadPartV2Output setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public UploadPartV2Output setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String getEtag() {
        return etag;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public UploadPartV2Output setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
        return this;
    }

    public String getServerSideEncryptionKeyID() {
        return serverSideEncryptionKeyID;
    }

    public UploadPartV2Output setServerSideEncryptionKeyID(String serverSideEncryptionKeyID) {
        this.serverSideEncryptionKeyID = serverSideEncryptionKeyID;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    @Override
    public String toString() {
        return "UploadPartV2Output{" +
                "requestInfo=" + requestInfo +
                ", partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", serverSideEncryptionKeyID='" + serverSideEncryptionKeyID + '\'' +
                ", hashCrc64ecma=" + hashCrc64ecma +
                '}';
    }
}
