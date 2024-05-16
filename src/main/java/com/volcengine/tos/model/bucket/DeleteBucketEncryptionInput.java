package com.volcengine.tos.model.bucket;

public class DeleteBucketEncryptionInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketEncryptionInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketEncryptionInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}
