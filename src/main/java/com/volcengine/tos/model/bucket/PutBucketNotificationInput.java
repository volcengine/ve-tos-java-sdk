package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutBucketNotificationInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("CloudFunctionConfigurations")
    private List<CloudFunctionConfiguration> cloudFunctionConfigurations;
    @JsonProperty("RocketMQConfigurations")
    private List<RocketMQConfiguration> rocketMQConfigurations;

    public String getBucket() {
        return bucket;
    }

    public PutBucketNotificationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<CloudFunctionConfiguration> getCloudFunctionConfigurations() {
        return cloudFunctionConfigurations;
    }

    public PutBucketNotificationInput setCloudFunctionConfigurations(List<CloudFunctionConfiguration> cloudFunctionConfigurations) {
        this.cloudFunctionConfigurations = cloudFunctionConfigurations;
        return this;
    }

    public List<RocketMQConfiguration> getRocketMQConfigurations() {
        return rocketMQConfigurations;
    }

    public PutBucketNotificationInput setRocketMQConfigurations(List<RocketMQConfiguration> rocketMQConfigurations) {
        this.rocketMQConfigurations = rocketMQConfigurations;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketNotificationInput{" +
                "bucket='" + bucket + '\'' +
                ", cloudFunctionConfigurations=" + cloudFunctionConfigurations +
                ", rocketMQConfigurations=" + rocketMQConfigurations +
                '}';
    }

    public static PutBucketNotificationInputBuilder builder() {
        return new PutBucketNotificationInputBuilder();
    }

    public static final class PutBucketNotificationInputBuilder {
        private String bucket;
        private List<CloudFunctionConfiguration> cloudFunctionConfigurations;
        private List<RocketMQConfiguration> rocketMQConfigurations;

        private PutBucketNotificationInputBuilder() {
        }

        public PutBucketNotificationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketNotificationInputBuilder cloudFunctionConfigurations(List<CloudFunctionConfiguration> cloudFunctionConfigurations) {
            this.cloudFunctionConfigurations = cloudFunctionConfigurations;
            return this;
        }

        public PutBucketNotificationInputBuilder rocketMQConfigurations(List<RocketMQConfiguration> rocketMQConfigurations) {
            this.rocketMQConfigurations = rocketMQConfigurations;
            return this;
        }

        public PutBucketNotificationInput build() {
            PutBucketNotificationInput putBucketNotificationInput = new PutBucketNotificationInput();
            putBucketNotificationInput.setBucket(bucket);
            putBucketNotificationInput.setCloudFunctionConfigurations(cloudFunctionConfigurations);
            putBucketNotificationInput.setRocketMQConfigurations(rocketMQConfigurations);
            return putBucketNotificationInput;
        }
    }
}
