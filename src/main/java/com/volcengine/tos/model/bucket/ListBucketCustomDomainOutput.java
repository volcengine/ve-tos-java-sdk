package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListBucketCustomDomainOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("CustomDomainRules")
    private List<CustomDomainRule> rule;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBucketCustomDomainOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<CustomDomainRule> getRule() {
        return rule;
    }

    public ListBucketCustomDomainOutput setRule(List<CustomDomainRule> rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public String toString() {
        return "ListBucketCustomDomainOutput{" +
                "requestInfo=" + requestInfo +
                ", rule=" + rule +
                '}';
    }

    public static ListBucketCustomDomainOutputBuilder builder() {
        return new ListBucketCustomDomainOutputBuilder();
    }

    public static final class ListBucketCustomDomainOutputBuilder {
        private RequestInfo requestInfo;
        private List<CustomDomainRule> rule;

        private ListBucketCustomDomainOutputBuilder() {
        }

        public ListBucketCustomDomainOutputBuilder requestInfo(RequestInfo requestInfo) {
            this.requestInfo = requestInfo;
            return this;
        }

        public ListBucketCustomDomainOutputBuilder rule(List<CustomDomainRule> rule) {
            this.rule = rule;
            return this;
        }

        public ListBucketCustomDomainOutput build() {
            ListBucketCustomDomainOutput listBucketCustomDomainOutput = new ListBucketCustomDomainOutput();
            listBucketCustomDomainOutput.setRequestInfo(requestInfo);
            listBucketCustomDomainOutput.setRule(rule);
            return listBucketCustomDomainOutput;
        }
    }
}
