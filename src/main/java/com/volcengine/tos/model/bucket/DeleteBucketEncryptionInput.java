package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteBucketEncryptionInput extends GenericInput {
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
