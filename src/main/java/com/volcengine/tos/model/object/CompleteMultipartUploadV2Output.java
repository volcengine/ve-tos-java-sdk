package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class CompleteMultipartUploadV2Output {
    private RequestInfo requestInfo;
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("CompletedParts")
    private List<UploadedPartV2> uploadedPartV2List;
    private String versionID;
    private String serverSideEncryption;
    private String serverSideEncryptionKeyID;
    private String hashCrc64ecma;
    private String callbackResult;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CompleteMultipartUploadV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public CompleteMultipartUploadV2Output setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CompleteMultipartUploadV2Output setKey(String key) {
        this.key = key;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public CompleteMultipartUploadV2Output setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public CompleteMultipartUploadV2Output setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public CompleteMultipartUploadV2Output setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public CompleteMultipartUploadV2Output setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
        return this;
    }

    public String getServerSideEncryptionKeyID() {
        return serverSideEncryptionKeyID;
    }

    public CompleteMultipartUploadV2Output setServerSideEncryptionKeyID(String serverSideEncryptionKeyID) {
        this.serverSideEncryptionKeyID = serverSideEncryptionKeyID;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public CompleteMultipartUploadV2Output setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public List<UploadedPartV2> getUploadedPartV2List() {
        return uploadedPartV2List;
    }

    public CompleteMultipartUploadV2Output setUploadedPartV2List(List<UploadedPartV2> uploadedPartV2List) {
        this.uploadedPartV2List = uploadedPartV2List;
        return this;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public CompleteMultipartUploadV2Output setCallbackResult(String callbackResult) {
        this.callbackResult = callbackResult;
        return this;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadV2Output{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", etag='" + etag + '\'' +
                ", location='" + location + '\'' +
                ", uploadedPartV2List=" + uploadedPartV2List +
                ", versionID='" + versionID + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", serverSideEncryptionKeyID='" + serverSideEncryptionKeyID + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", callbackResult='" + callbackResult + '\'' +
                '}';
    }
}
