package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutBucketEncryptionInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Rule")
    private BucketEncryptionRule rule;

    public String getBucket() {
        return bucket;
    }

    public PutBucketEncryptionInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public BucketEncryptionRule getRule() {
        return rule;
    }

    public PutBucketEncryptionInput setRule(BucketEncryptionRule rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketEncryptionInput{" +
                "bucket='" + bucket + '\'' +
                ", rule=" + rule +
                '}';
    }
}
