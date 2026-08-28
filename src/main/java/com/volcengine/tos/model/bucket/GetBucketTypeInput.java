package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketTypeInput extends GenericInput {
    private String bucket;

    public GetBucketTypeInput() {
    }

    public GetBucketTypeInput(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public GetBucketTypeInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTypeInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}