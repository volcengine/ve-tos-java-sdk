package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteBucketTaggingInput extends GenericInput {
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
