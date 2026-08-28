package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import java.util.List;

public class GetConvertWorkflowOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("Rules")
    private List<ConvertWorkflowRule> rules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetConvertWorkflowOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<ConvertWorkflowRule> getRules() {
        return rules;
    }

    public GetConvertWorkflowOutput setRules(List<ConvertWorkflowRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "GetConvertWorkflowOutput{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                '}';
    }
}