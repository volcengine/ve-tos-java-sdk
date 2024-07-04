package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketNotificationType2Input extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Rules")
    private List<NotificationRule> rules;
    @JsonProperty("Version")
    private String version;

    public PutBucketNotificationType2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public List<NotificationRule> getRules() {
        return rules;
    }

    public PutBucketNotificationType2Input setRules(List<NotificationRule> rules) {
        this.rules = rules;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PutBucketNotificationType2Input setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketNotificationType2Input{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                ", version='" + version + '\'' +
                '}';
    }

    public static PutBucketNotificationType2InputBuilder builder() {
        return new PutBucketNotificationType2InputBuilder();
    }

    public static final class PutBucketNotificationType2InputBuilder {
        private String bucket;
        private List<NotificationRule> rules;
        private String version;

        private PutBucketNotificationType2InputBuilder() {
        }

        public PutBucketNotificationType2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketNotificationType2InputBuilder rules(List<NotificationRule> notificationRule) {
            this.rules = notificationRule;
            return this;
        }

        public PutBucketNotificationType2InputBuilder version(String version) {
            this.version = version;
            return this;
        }

        public PutBucketNotificationType2Input build() {
            PutBucketNotificationType2Input putBucketNotificationType2Input = new PutBucketNotificationType2Input();
            putBucketNotificationType2Input.setBucket(bucket);
            putBucketNotificationType2Input.setRules(rules);
            putBucketNotificationType2Input.setVersion(version);
            return putBucketNotificationType2Input;
        }
    }
}
