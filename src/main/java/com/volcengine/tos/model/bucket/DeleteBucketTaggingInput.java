package com.volcengine.tos.model.bucket;

public class DeleteBucketTaggingInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketTaggingInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}
