package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketReplicationInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Role")
    private String role;
    @JsonProperty("Rules")
    private List<ReplicationRule> rules;

    public String getBucket() {
        return bucket;
    }

    public PutBucketReplicationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getRole() {
        return role;
    }

    public PutBucketReplicationInput setRole(String role) {
        this.role = role;
        return this;
    }

    public List<ReplicationRule> getRules() {
        return rules;
    }

    public PutBucketReplicationInput setRules(List<ReplicationRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketReplicationInput{" +
                "bucket='" + bucket + '\'' +
                ", role='" + role + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static PutBucketReplicationInputBuilder builder() {
        return new PutBucketReplicationInputBuilder();
    }

    public static final class PutBucketReplicationInputBuilder {
        private String bucket;
        private String role;
        private List<ReplicationRule> rules;

        private PutBucketReplicationInputBuilder() {
        }

        public PutBucketReplicationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketReplicationInputBuilder role(String role) {
            this.role = role;
            return this;
        }

        public PutBucketReplicationInputBuilder rules(List<ReplicationRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutBucketReplicationInput build() {
            PutBucketReplicationInput putBucketReplicationInput = new PutBucketReplicationInput();
            putBucketReplicationInput.setBucket(bucket);
            putBucketReplicationInput.setRole(role);
            putBucketReplicationInput.setRules(rules);
            return putBucketReplicationInput;
        }
    }
}
