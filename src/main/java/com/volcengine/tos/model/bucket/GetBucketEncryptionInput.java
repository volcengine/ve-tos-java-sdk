package com.volcengine.tos.model.bucket;

public class GetBucketEncryptionInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketEncryptionInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketEncryptionInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}
