package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;


public class PutObjectOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("VersionId")
    private String versionID;
    @Deprecated
    private String crc64;
    private String hashCrc64ecma;
    private String sseCustomerAlgorithm;
    private String sseCustomerKeyMD5;
    private String sseCustomerKey;
    private String serverSideEncryption;
    private String serverSideEncryptionKeyID;

    private String callbackResult;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public PutObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public PutObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Deprecated
    public String getCrc64() {
        return hashCrc64ecma;
    }

    @Deprecated
    public PutObjectOutput setCrc64(String crc64) {
        this.hashCrc64ecma = crc64;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public PutObjectOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public PutObjectOutput setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
        return this;
    }

    public String getSseCustomerKeyMD5() {
        return sseCustomerKeyMD5;
    }

    public PutObjectOutput setSseCustomerKeyMD5(String sseCustomerKeyMD5) {
        this.sseCustomerKeyMD5 = sseCustomerKeyMD5;
        return this;
    }

    public String getSseCustomerKey() {
        return sseCustomerKey;
    }

    public PutObjectOutput setSseCustomerKey(String sseCustomerKey) {
        this.sseCustomerKey = sseCustomerKey;
        return this;
    }

    public String getServerSideEncryptionKeyID() {
        return serverSideEncryptionKeyID;
    }

    public PutObjectOutput setServerSideEncryptionKeyID(String serverSideEncryptionKeyID) {
        this.serverSideEncryptionKeyID = serverSideEncryptionKeyID;
        return this;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public PutObjectOutput setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
        return this;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public PutObjectOutput setCallbackResult(String callbackResult) {
        this.callbackResult = callbackResult;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", etag='" + etag + '\'' +
                ", versionID='" + versionID + '\'' +
                ", hashCrc64ecma=" + hashCrc64ecma +
                ", sseCustomerAlgorithm='" + sseCustomerAlgorithm + '\'' +
                ", sseCustomerKeyMD5='" + sseCustomerKeyMD5 + '\'' +
                ", sseCustomerKey='" + sseCustomerKey + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", serverSideEncryptionKeyID='" + serverSideEncryptionKeyID + '\'' +
                ", callbackResult='" + callbackResult + '\'' +
                '}';
    }
}
