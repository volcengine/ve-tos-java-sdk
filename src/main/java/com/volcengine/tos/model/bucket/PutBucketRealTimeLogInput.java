package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutBucketRealTimeLogInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("RealTimeLogConfiguration")
    private RealTimeLogConfiguration configuration;

    public String getBucket() {
        return bucket;
    }

    public PutBucketRealTimeLogInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public RealTimeLogConfiguration getConfiguration() {
        return configuration;
    }

    public PutBucketRealTimeLogInput setConfiguration(RealTimeLogConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketRealTimeLogInput{" +
                "bucket='" + bucket + '\'' +
                ", configuration=" + configuration +
                '}';
    }

    public static PutBucketRealTimeLogInputBuilder builder() {
        return new PutBucketRealTimeLogInputBuilder();
    }

    public static final class PutBucketRealTimeLogInputBuilder {
        private String bucket;
        private RealTimeLogConfiguration configuration;

        private PutBucketRealTimeLogInputBuilder() {
        }

        public PutBucketRealTimeLogInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketRealTimeLogInputBuilder configuration(RealTimeLogConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public PutBucketRealTimeLogInput build() {
            PutBucketRealTimeLogInput putBucketRealTimeLogInput = new PutBucketRealTimeLogInput();
            putBucketRealTimeLogInput.setBucket(bucket);
            putBucketRealTimeLogInput.setConfiguration(configuration);
            return putBucketRealTimeLogInput;
        }
    }
}
