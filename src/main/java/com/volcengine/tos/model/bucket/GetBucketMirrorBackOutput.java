package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketMirrorBackOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Rules")
    private List<MirrorBackRule> rules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketMirrorBackOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<MirrorBackRule> getRules() {
        return rules;
    }

    public GetBucketMirrorBackOutput setRules(List<MirrorBackRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketMirrorBackOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                '}';
    }
}
