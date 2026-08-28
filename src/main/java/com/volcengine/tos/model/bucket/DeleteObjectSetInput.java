package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteObjectSetInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public DeleteObjectSetInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public DeleteObjectSetInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectSetInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static DeleteObjectSetInputBuilder builder() {
        return new DeleteObjectSetInputBuilder();
    }

    public static final class DeleteObjectSetInputBuilder {
        private String bucket;
        private String objectSetName;

        private DeleteObjectSetInputBuilder() {
        }

        public DeleteObjectSetInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteObjectSetInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public DeleteObjectSetInput build() {
            DeleteObjectSetInput input = new DeleteObjectSetInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
