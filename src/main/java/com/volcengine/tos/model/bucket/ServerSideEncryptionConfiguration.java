package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerSideEncryptionConfiguration {
    @JsonProperty("Rule")
    private BucketEncryptionRule rule;

    public BucketEncryptionRule getRule() {
        return rule;
    }

    public ServerSideEncryptionConfiguration setRule(BucketEncryptionRule rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public String toString() {
        return "ServerSideEncryptionConfiguration{" +
                "rule=" + rule +
                '}';
    }
}