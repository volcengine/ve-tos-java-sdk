package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketAccessMonitorInput extends GenericInput {
    private String bucket;

    public GetBucketAccessMonitorInput() {
    }

    public GetBucketAccessMonitorInput(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public GetBucketAccessMonitorInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketAccessMonitorInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}