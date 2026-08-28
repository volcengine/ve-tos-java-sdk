package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetBucketObjectSetConfigurationInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketObjectSetConfigurationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketObjectSetConfigurationInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketObjectSetConfigurationInputBuilder builder() {
        return new GetBucketObjectSetConfigurationInputBuilder();
    }

    public static final class GetBucketObjectSetConfigurationInputBuilder {
        private String bucket;

        private GetBucketObjectSetConfigurationInputBuilder() {
        }

        public GetBucketObjectSetConfigurationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketObjectSetConfigurationInput build() {
            GetBucketObjectSetConfigurationInput input = new GetBucketObjectSetConfigurationInput();
            input.setBucket(bucket);
            return input;
        }
    }
}
