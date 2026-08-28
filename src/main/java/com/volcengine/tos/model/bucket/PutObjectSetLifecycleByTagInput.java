package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutObjectSetLifecycleByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("ObjectSetTagRules")
    private List<ObjectSetTagLifecycleRule> objectSetTagRules;

    @JsonIgnore
    private boolean allowSameActionOverlap;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetLifecycleByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<ObjectSetTagLifecycleRule> getObjectSetTagRules() {
        return objectSetTagRules;
    }

    public PutObjectSetLifecycleByTagInput setObjectSetTagRules(List<ObjectSetTagLifecycleRule> objectSetTagRules) {
        this.objectSetTagRules = objectSetTagRules;
        return this;
    }

    public boolean isAllowSameActionOverlap() {
        return allowSameActionOverlap;
    }

    public PutObjectSetLifecycleByTagInput setAllowSameActionOverlap(boolean allowSameActionOverlap) {
        this.allowSameActionOverlap = allowSameActionOverlap;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetLifecycleByTagInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetTagRules=" + objectSetTagRules +
                ", allowSameActionOverlap=" + allowSameActionOverlap +
                '}';
    }

    public static PutObjectSetLifecycleByTagInputBuilder builder() {
        return new PutObjectSetLifecycleByTagInputBuilder();
    }

    public static final class PutObjectSetLifecycleByTagInputBuilder {
        private String bucket;
        private List<ObjectSetTagLifecycleRule> objectSetTagRules;
        private boolean allowSameActionOverlap;

        private PutObjectSetLifecycleByTagInputBuilder() {
        }

        public PutObjectSetLifecycleByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetLifecycleByTagInputBuilder objectSetTagRules(List<ObjectSetTagLifecycleRule> objectSetTagRules) {
            this.objectSetTagRules = objectSetTagRules;
            return this;
        }

        public PutObjectSetLifecycleByTagInputBuilder allowSameActionOverlap(boolean allowSameActionOverlap) {
            this.allowSameActionOverlap = allowSameActionOverlap;
            return this;
        }

        public PutObjectSetLifecycleByTagInput build() {
            PutObjectSetLifecycleByTagInput input = new PutObjectSetLifecycleByTagInput();
            input.setBucket(bucket);
            input.setObjectSetTagRules(objectSetTagRules);
            input.setAllowSameActionOverlap(allowSameActionOverlap);
            return input;
        }
    }
}
