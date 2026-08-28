package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetLifecycleInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetLifecycleInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static GetObjectSetLifecycleInputBuilder builder() {
        return new GetObjectSetLifecycleInputBuilder();
    }

    public static final class GetObjectSetLifecycleInputBuilder {
        private String bucket;
        private String objectSetName;

        private GetObjectSetLifecycleInputBuilder() {
        }

        public GetObjectSetLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetLifecycleInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public GetObjectSetLifecycleInput build() {
            GetObjectSetLifecycleInput input = new GetObjectSetLifecycleInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
