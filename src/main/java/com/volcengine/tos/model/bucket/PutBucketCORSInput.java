package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketCORSInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("CORSRules")
    private List<CORSRule> rules;

    public String getBucket() {
        return bucket;
    }

    public PutBucketCORSInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<CORSRule> getRules() {
        return rules;
    }

    public PutBucketCORSInput setRules(List<CORSRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketCORS{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static PutBucketCORSBuilder builder() {
        return new PutBucketCORSBuilder();
    }

    public static final class PutBucketCORSBuilder {
        private String bucket;
        private List<CORSRule> rules;

        private PutBucketCORSBuilder() {
        }

        public PutBucketCORSBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketCORSBuilder rules(List<CORSRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutBucketCORSInput build() {
            PutBucketCORSInput putBucketCORSInput = new PutBucketCORSInput();
            putBucketCORSInput.setBucket(bucket);
            putBucketCORSInput.setRules(rules);
            return putBucketCORSInput;
        }
    }
}
