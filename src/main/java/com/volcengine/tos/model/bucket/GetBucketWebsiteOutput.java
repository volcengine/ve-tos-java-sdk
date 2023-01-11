package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketWebsiteOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("RedirectAllRequestsTo")
    private RedirectAllRequestsTo redirectAllRequestsTo;
    @JsonProperty("IndexDocument")
    private IndexDocument indexDocument;
    @JsonProperty("ErrorDocument")
    private ErrorDocument errorDocument;
    @JsonProperty("RoutingRules")
    private List<RoutingRule> routingRules;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketWebsiteOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public RedirectAllRequestsTo getRedirectAllRequestsTo() {
        return redirectAllRequestsTo;
    }

    public GetBucketWebsiteOutput setRedirectAllRequestsTo(RedirectAllRequestsTo redirectAllRequestsTo) {
        this.redirectAllRequestsTo = redirectAllRequestsTo;
        return this;
    }

    public IndexDocument getIndexDocument() {
        return indexDocument;
    }

    public GetBucketWebsiteOutput setIndexDocument(IndexDocument indexDocument) {
        this.indexDocument = indexDocument;
        return this;
    }

    public ErrorDocument getErrorDocument() {
        return errorDocument;
    }

    public GetBucketWebsiteOutput setErrorDocument(ErrorDocument errorDocument) {
        this.errorDocument = errorDocument;
        return this;
    }

    public List<RoutingRule> getRoutingRules() {
        return routingRules;
    }

    public GetBucketWebsiteOutput setRoutingRules(List<RoutingRule> routingRules) {
        this.routingRules = routingRules;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketWebsiteOutput{" +
                "requestInfo=" + requestInfo +
                ", redirectAllRequestsTo=" + redirectAllRequestsTo +
                ", indexDocument=" + indexDocument +
                ", errorDocument=" + errorDocument +
                ", routingRules=" + routingRules +
                '}';
    }
}
