package com.volcengine.tos.model.object;

public class UploadFileOutput {
    private CompleteMultipartUploadOutput completeMultipartUploadOutput;
    private String bucket;
    private String objectKey;
    private String uploadID;

    public CompleteMultipartUploadOutput getCompleteMultipartUploadOutput() {
        return completeMultipartUploadOutput;
    }

    public UploadFileOutput setCompleteMultipartUploadOutput(CompleteMultipartUploadOutput completeMultipartUploadOutput) {
        this.completeMultipartUploadOutput = completeMultipartUploadOutput;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public UploadFileOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public UploadFileOutput setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadFileOutput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFileOutput{" +
                "completeMultipartUploadOutput=" + completeMultipartUploadOutput +
                ", bucket='" + bucket + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", uploadID='" + uploadID + '\'' +
                '}';
    }
}
