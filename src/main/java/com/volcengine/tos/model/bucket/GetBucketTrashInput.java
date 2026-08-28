package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketTrashInput extends GenericInput {
    private String bucket;

    public GetBucketTrashInput() {
    }

    public GetBucketTrashInput(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public GetBucketTrashInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTrashInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}
