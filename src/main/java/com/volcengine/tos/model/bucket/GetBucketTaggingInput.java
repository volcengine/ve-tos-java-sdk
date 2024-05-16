package com.volcengine.tos.model.bucket;

public class GetBucketTaggingInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTaggingInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}
