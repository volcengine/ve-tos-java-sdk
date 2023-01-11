package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class ResumableCopyObjectOutput {
    private RequestInfo requestInfo;
    private String bucket;
    private String key;
    private String uploadID;
    private String etag;
    private String location;
    private String versionID;
    private String hashCrc64ecma;
    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String encodingType;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ResumableCopyObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public ResumableCopyObjectOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ResumableCopyObjectOutput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ResumableCopyObjectOutput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public ResumableCopyObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ResumableCopyObjectOutput setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ResumableCopyObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public ResumableCopyObjectOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public ResumableCopyObjectOutput setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public ResumableCopyObjectOutput setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ResumableCopyObjectOutput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ResumableCopyObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", etag='" + etag + '\'' +
                ", location='" + location + '\'' +
                ", versionID='" + versionID + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }
}
