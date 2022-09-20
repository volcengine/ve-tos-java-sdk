package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

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
    private String versionID;
    private String hashCrc64ecma;

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

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public CompleteMultipartUploadV2Output setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
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
                ", versionID='" + versionID + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                '}';
    }
}
