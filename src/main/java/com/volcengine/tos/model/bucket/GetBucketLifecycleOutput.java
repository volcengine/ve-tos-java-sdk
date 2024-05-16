package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketLifecycleOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Rules")
    private List<LifecycleRule> rules;

    @JsonIgnore
    private boolean allowSameActionOverlap;
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketLifecycleOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<LifecycleRule> getRules() {
        return rules;
    }

    public GetBucketLifecycleOutput setRules(List<LifecycleRule> rules) {
        this.rules = rules;
        return this;
    }

    public boolean isAllowSameActionOverlap() {
        return allowSameActionOverlap;
    }

    public GetBucketLifecycleOutput setAllowSameActionOverlap(boolean allowSameActionOverlap) {
        this.allowSameActionOverlap = allowSameActionOverlap;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketLifecycleOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                ", allowSameActionOverlap=" + allowSameActionOverlap +
                '}';
    }
}
