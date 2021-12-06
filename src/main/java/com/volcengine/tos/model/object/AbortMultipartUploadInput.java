package com.volcengine.tos.model.object;


public class AbortMultipartUploadInput {
    private String key;
    private String uploadID;

    public AbortMultipartUploadInput() {
    }

    public AbortMultipartUploadInput(String key, String uploadID) {
        this.key = key;
        this.uploadID = uploadID;
    }

    public String getKey() {
        return key;
    }

    public AbortMultipartUploadInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public AbortMultipartUploadInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                '}';
    }
}
