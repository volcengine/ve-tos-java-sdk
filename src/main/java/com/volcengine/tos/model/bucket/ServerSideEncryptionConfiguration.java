package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerSideEncryptionConfiguration {

    @JsonProperty("Rule")
    private BucketEncryptionRule rule;

    // Getter and Setter

    public BucketEncryptionRule getRule() {
        return rule;
    }

    public void setRule(BucketEncryptionRule rule) {
        this.rule = rule;
    }
}