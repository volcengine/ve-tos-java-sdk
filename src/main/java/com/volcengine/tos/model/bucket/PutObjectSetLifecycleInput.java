package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutObjectSetLifecycleInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    @JsonProperty("Rules")
    private List<LifecycleRule> rules;

    @JsonIgnore
    private boolean allowSameActionOverlap;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public PutObjectSetLifecycleInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public List<LifecycleRule> getRules() {
        return rules;
    }

    public PutObjectSetLifecycleInput setRules(List<LifecycleRule> rules) {
        this.rules = rules;
        return this;
    }

    public boolean isAllowSameActionOverlap() {
        return allowSameActionOverlap;
    }

    public PutObjectSetLifecycleInput setAllowSameActionOverlap(boolean allowSameActionOverlap) {
        this.allowSameActionOverlap = allowSameActionOverlap;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                ", rules=" + rules +
                ", allowSameActionOverlap=" + allowSameActionOverlap +
                '}';
    }

    public static PutObjectSetLifecycleInputBuilder builder() {
        return new PutObjectSetLifecycleInputBuilder();
    }

    public static final class PutObjectSetLifecycleInputBuilder {
        private String bucket;
        private String objectSetName;
        private List<LifecycleRule> rules;
        private boolean allowSameActionOverlap;

        private PutObjectSetLifecycleInputBuilder() {
        }

        public PutObjectSetLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetLifecycleInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public PutObjectSetLifecycleInputBuilder rules(List<LifecycleRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutObjectSetLifecycleInputBuilder allowSameActionOverlap(boolean allowSameActionOverlap) {
            this.allowSameActionOverlap = allowSameActionOverlap;
            return this;
        }

        public PutObjectSetLifecycleInput build() {
            PutObjectSetLifecycleInput input = new PutObjectSetLifecycleInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            input.setRules(rules);
            input.setAllowSameActionOverlap(allowSameActionOverlap);
            return input;
        }
    }
}
