package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PutBucketMirrorBackInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Rules")
    private List<MirrorBackRule> rules;

    public String getBucket() {
        return bucket;
    }

    public PutBucketMirrorBackInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<MirrorBackRule> getRules() {
        return rules;
    }

    public PutBucketMirrorBackInput setRules(List<MirrorBackRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketMirrorBackInput{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static PutBucketMirrorBackInputBuilder builder() {
        return new PutBucketMirrorBackInputBuilder();
    }

    public static final class PutBucketMirrorBackInputBuilder {
        private String bucket;
        private List<MirrorBackRule> rules;

        private PutBucketMirrorBackInputBuilder() {
        }

        public PutBucketMirrorBackInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketMirrorBackInputBuilder rules(List<MirrorBackRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutBucketMirrorBackInput build() {
            PutBucketMirrorBackInput putBucketMirrorBackInput = new PutBucketMirrorBackInput();
            putBucketMirrorBackInput.setBucket(bucket);
            putBucketMirrorBackInput.setRules(rules);
            return putBucketMirrorBackInput;
        }
    }
}
