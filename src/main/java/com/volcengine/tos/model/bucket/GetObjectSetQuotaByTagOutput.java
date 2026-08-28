package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetObjectSetQuotaByTagOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Rules")
    private List<ObjectSetQuotaByTagRule> rules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetQuotaByTagOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ObjectSetQuotaByTagRule> getRules() {
        return rules;
    }

    public GetObjectSetQuotaByTagOutput setRules(List<ObjectSetQuotaByTagRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetQuotaByTagOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                '}';
    }
}
