package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class ListBucketInventoryInput extends GenericInput {
    private String bucket;
    private String continuationToken;

    public String getBucket() {
        return bucket;
    }

    public ListBucketInventoryInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public ListBucketInventoryInput setContinuationToken(String continuationToken) {
        this.continuationToken = continuationToken;
        return this;
    }

    @Override
    public String toString() {
        return "ListBucketInventoryInput{" +
                "bucket='" + bucket + '\'' +
                ", continuationToken='" + continuationToken + '\'' +
                '}';
    }
}
