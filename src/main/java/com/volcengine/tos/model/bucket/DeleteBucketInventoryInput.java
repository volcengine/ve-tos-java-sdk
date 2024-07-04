package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteBucketInventoryInput extends GenericInput {
    private String bucket;
    private String id;
    public String getBucket() {
        return bucket;
    }

    public DeleteBucketInventoryInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public DeleteBucketInventoryInput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketInventoryInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
