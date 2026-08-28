package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetObjectSetLifecycleByTagOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("ObjectSetTagRules")
    private List<ObjectSetTagLifecycleRule> objectSetTagRules;

    @JsonIgnore
    private boolean allowSameActionOverlap;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetLifecycleByTagOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ObjectSetTagLifecycleRule> getObjectSetTagRules() {
        return objectSetTagRules;
    }

    public GetObjectSetLifecycleByTagOutput setObjectSetTagRules(List<ObjectSetTagLifecycleRule> objectSetTagRules) {
        this.objectSetTagRules = objectSetTagRules;
        return this;
    }

    public boolean isAllowSameActionOverlap() {
        return allowSameActionOverlap;
    }

    public GetObjectSetLifecycleByTagOutput setAllowSameActionOverlap(boolean allowSameActionOverlap) {
        this.allowSameActionOverlap = allowSameActionOverlap;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetLifecycleByTagOutput{" +
                "requestInfo=" + requestInfo +
                ", objectSetTagRules=" + objectSetTagRules +
                ", allowSameActionOverlap=" + allowSameActionOverlap +
                '}';
    }
}
