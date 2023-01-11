package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMultipartUploadOutputJson {
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("UploadId")
    private String uploadID;
    @JsonProperty("EncodingType")
    private String encodingType;

    public String getBucket() {
        return bucket;
    }

    public CreateMultipartUploadOutputJson bucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CreateMultipartUploadOutputJson key(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public CreateMultipartUploadOutputJson uploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public CreateMultipartUploadOutputJson encodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }
}
