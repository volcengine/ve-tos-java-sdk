package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutBucketCustomDomainInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("CustomDomainRule")
    private CustomDomainRule rule;

    public String getBucket() {
        return bucket;
    }

    public PutBucketCustomDomainInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CustomDomainRule getRule() {
        return rule;
    }

    public PutBucketCustomDomainInput setRule(CustomDomainRule rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketCustomDomainInput{" +
                "bucket='" + bucket + '\'' +
                ", rule=" + rule +
                '}';
    }

    public static PutBucketCustomDomainInputBuilder builder() {
        return new PutBucketCustomDomainInputBuilder();
    }

    public static final class PutBucketCustomDomainInputBuilder {
        private String bucket;
        private CustomDomainRule rule;

        private PutBucketCustomDomainInputBuilder() {
        }

        public PutBucketCustomDomainInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketCustomDomainInputBuilder rule(CustomDomainRule rule) {
            this.rule = rule;
            return this;
        }

        public PutBucketCustomDomainInput build() {
            PutBucketCustomDomainInput putBucketCustomDomainInput = new PutBucketCustomDomainInput();
            putBucketCustomDomainInput.setBucket(bucket);
            putBucketCustomDomainInput.setRule(rule);
            return putBucketCustomDomainInput;
        }
    }
}
