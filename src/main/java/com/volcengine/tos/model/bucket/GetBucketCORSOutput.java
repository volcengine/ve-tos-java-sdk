package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketCORSOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("CORSRules")
    private List<CORSRule> rules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketCORSOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<CORSRule> getRules() {
        return rules;
    }

    public GetBucketCORSOutput setRules(List<CORSRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketCORSOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                '}';
    }
}
