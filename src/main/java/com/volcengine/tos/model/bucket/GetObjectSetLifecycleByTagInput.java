package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetLifecycleByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetLifecycleByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetLifecycleByTagInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetObjectSetLifecycleByTagInputBuilder builder() {
        return new GetObjectSetLifecycleByTagInputBuilder();
    }

    public static final class GetObjectSetLifecycleByTagInputBuilder {
        private String bucket;

        private GetObjectSetLifecycleByTagInputBuilder() {
        }

        public GetObjectSetLifecycleByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetLifecycleByTagInput build() {
            GetObjectSetLifecycleByTagInput input = new GetObjectSetLifecycleByTagInput();
            input.setBucket(bucket);
            return input;
        }
    }
}
