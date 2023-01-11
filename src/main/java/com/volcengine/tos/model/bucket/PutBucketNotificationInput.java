package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PutBucketNotificationInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("CloudFunctionConfigurations")
    private List<CloudFunctionConfiguration> cloudFunctionConfigurations;

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

    @Override
    public String toString() {
        return "PutBucketNotificationInput{" +
                "bucket='" + bucket + '\'' +
                ", cloudFunctionConfigurations=" + cloudFunctionConfigurations +
                '}';
    }

    public static PutBucketNotificationInputBuilder builder() {
        return new PutBucketNotificationInputBuilder();
    }

    public static final class PutBucketNotificationInputBuilder {
        private String bucket;
        private List<CloudFunctionConfiguration> cloudFunctionConfigurations;

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

        public PutBucketNotificationInput build() {
            PutBucketNotificationInput putBucketNotificationInput = new PutBucketNotificationInput();
            putBucketNotificationInput.setBucket(bucket);
            putBucketNotificationInput.setCloudFunctionConfigurations(cloudFunctionConfigurations);
            return putBucketNotificationInput;
        }
    }
}
