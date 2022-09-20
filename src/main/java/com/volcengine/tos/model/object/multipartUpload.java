package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class multipartUpload {
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("UploadId")
    private String uploadID;

    public String getBucket() {
        return bucket;
    }

    public multipartUpload setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public multipartUpload setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public multipartUpload setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    @Override
    public String toString() {
        return "multipartUpload{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                '}';
    }
}
