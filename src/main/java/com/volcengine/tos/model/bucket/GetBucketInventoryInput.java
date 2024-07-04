package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketInventoryInput extends GenericInput {
    private String bucket;

    private String id;

    public String getBucket() {
        return bucket;
    }

    public GetBucketInventoryInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetBucketInventoryInput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketInventoryInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
