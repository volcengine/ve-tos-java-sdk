package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketLifecycleInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Rules")
    private List<LifecycleRule> rules;
    @JsonIgnore
    private boolean allowSameActionOverlap;

    public String getBucket() {
        return bucket;
    }

    public PutBucketLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<LifecycleRule> getRules() {
        return rules;
    }

    public PutBucketLifecycleInput setRules(List<LifecycleRule> rules) {
        this.rules = rules;
        return this;
    }

    public boolean isAllowSameActionOverlap() {
        return allowSameActionOverlap;
    }

    public PutBucketLifecycleInput setAllowSameActionOverlap(boolean allowSameActionOverlap) {
        this.allowSameActionOverlap = allowSameActionOverlap;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static PutBucketLifecycleInputBuilder builder() {
        return new PutBucketLifecycleInputBuilder();
    }

    public static final class PutBucketLifecycleInputBuilder {
        private String bucket;
        private List<LifecycleRule> rules;
        private boolean allowSameActionOverlap;

        private PutBucketLifecycleInputBuilder() {
        }

        public PutBucketLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketLifecycleInputBuilder rules(List<LifecycleRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutBucketLifecycleInputBuilder allowSameActionOverlap(boolean allowSameActionOverlap) {
            this.allowSameActionOverlap = allowSameActionOverlap;
            return this;
        }

        public PutBucketLifecycleInput build() {
            PutBucketLifecycleInput putBucketLifecycleInput = new PutBucketLifecycleInput();
            putBucketLifecycleInput.setBucket(bucket);
            putBucketLifecycleInput.setRules(rules);
            putBucketLifecycleInput.setAllowSameActionOverlap(allowSameActionOverlap);
            return putBucketLifecycleInput;
        }
    }
}
