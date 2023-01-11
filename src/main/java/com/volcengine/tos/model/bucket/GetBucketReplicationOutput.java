package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketReplicationOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Rules")
    private List<ReplicationRule> rules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketReplicationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ReplicationRule> getRules() {
        return rules;
    }

    public GetBucketReplicationOutput setRules(List<ReplicationRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketReplicationOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                '}';
    }
}
