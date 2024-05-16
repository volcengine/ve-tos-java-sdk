package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketEncryptionOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Rule")
    private BucketEncryptionRule rule;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketEncryptionOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public BucketEncryptionRule getRule() {
        return rule;
    }

    public GetBucketEncryptionOutput setRule(BucketEncryptionRule rule) {
        this.rule = rule;
        return this;
    }
}
