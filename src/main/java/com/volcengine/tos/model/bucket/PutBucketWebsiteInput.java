package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketWebsiteInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("RedirectAllRequestsTo")
    private RedirectAllRequestsTo redirectAllRequestsTo;
    @JsonProperty("IndexDocument")
    private IndexDocument indexDocument;
    @JsonProperty("ErrorDocument")
    private ErrorDocument errorDocument;
    @JsonProperty("RoutingRules")
    private List<RoutingRule> routingRules;

    public String getBucket() {
        return bucket;
    }

    public PutBucketWebsiteInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public RedirectAllRequestsTo getRedirectAllRequestsTo() {
        return redirectAllRequestsTo;
    }

    public PutBucketWebsiteInput setRedirectAllRequestsTo(RedirectAllRequestsTo redirectAllRequestsTo) {
        this.redirectAllRequestsTo = redirectAllRequestsTo;
        return this;
    }

    public IndexDocument getIndexDocument() {
        return indexDocument;
    }

    public PutBucketWebsiteInput setIndexDocument(IndexDocument indexDocument) {
        this.indexDocument = indexDocument;
        return this;
    }

    public ErrorDocument getErrorDocument() {
        return errorDocument;
    }

    public PutBucketWebsiteInput setErrorDocument(ErrorDocument errorDocument) {
        this.errorDocument = errorDocument;
        return this;
    }

    public List<RoutingRule> getRoutingRules() {
        return routingRules;
    }

    public PutBucketWebsiteInput setRoutingRules(List<RoutingRule> routingRules) {
        this.routingRules = routingRules;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketWebsiteInput{" +
                "bucket='" + bucket + '\'' +
                ", redirectAllRequestsTo=" + redirectAllRequestsTo +
                ", indexDocument=" + indexDocument +
                ", errorDocument=" + errorDocument +
                ", routingRules=" + routingRules +
                '}';
    }

    public static PutBucketWebsiteInputBuilder builder() {
        return new PutBucketWebsiteInputBuilder();
    }

    public static final class PutBucketWebsiteInputBuilder {
        private String bucket;
        private RedirectAllRequestsTo redirectAllRequestsTo;
        private IndexDocument indexDocument;
        private ErrorDocument errorDocument;
        private List<RoutingRule> routingRules;

        private PutBucketWebsiteInputBuilder() {
        }

        public PutBucketWebsiteInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketWebsiteInputBuilder redirectAllRequestsTo(RedirectAllRequestsTo redirectAllRequestsTo) {
            this.redirectAllRequestsTo = redirectAllRequestsTo;
            return this;
        }

        public PutBucketWebsiteInputBuilder indexDocument(IndexDocument indexDocument) {
            this.indexDocument = indexDocument;
            return this;
        }

        public PutBucketWebsiteInputBuilder errorDocument(ErrorDocument errorDocument) {
            this.errorDocument = errorDocument;
            return this;
        }

        public PutBucketWebsiteInputBuilder routingRules(List<RoutingRule> routingRules) {
            this.routingRules = routingRules;
            return this;
        }

        public PutBucketWebsiteInput build() {
            PutBucketWebsiteInput putBucketWebsiteInput = new PutBucketWebsiteInput();
            putBucketWebsiteInput.setBucket(bucket);
            putBucketWebsiteInput.setRedirectAllRequestsTo(redirectAllRequestsTo);
            putBucketWebsiteInput.setIndexDocument(indexDocument);
            putBucketWebsiteInput.setErrorDocument(errorDocument);
            putBucketWebsiteInput.setRoutingRules(routingRules);
            return putBucketWebsiteInput;
        }
    }
}
