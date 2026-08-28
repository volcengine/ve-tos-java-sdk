package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteObjectSetLifecycleInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public DeleteObjectSetLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public DeleteObjectSetLifecycleInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectSetLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static DeleteObjectSetLifecycleInputBuilder builder() {
        return new DeleteObjectSetLifecycleInputBuilder();
    }

    public static final class DeleteObjectSetLifecycleInputBuilder {
        private String bucket;
        private String objectSetName;

        private DeleteObjectSetLifecycleInputBuilder() {
        }

        public DeleteObjectSetLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteObjectSetLifecycleInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public DeleteObjectSetLifecycleInput build() {
            DeleteObjectSetLifecycleInput input = new DeleteObjectSetLifecycleInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
