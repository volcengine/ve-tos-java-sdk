package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteObjectSetLifecycleByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteObjectSetLifecycleByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectSetLifecycleByTagInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteObjectSetLifecycleByTagInputBuilder builder() {
        return new DeleteObjectSetLifecycleByTagInputBuilder();
    }

    public static final class DeleteObjectSetLifecycleByTagInputBuilder {
        private String bucket;

        private DeleteObjectSetLifecycleByTagInputBuilder() {
        }

        public DeleteObjectSetLifecycleByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteObjectSetLifecycleByTagInput build() {
            DeleteObjectSetLifecycleByTagInput input = new DeleteObjectSetLifecycleByTagInput();
            input.setBucket(bucket);
            return input;
        }
    }
}
