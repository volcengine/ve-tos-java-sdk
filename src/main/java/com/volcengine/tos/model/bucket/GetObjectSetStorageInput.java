package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetStorageInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetStorageInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetStorageInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetStorageInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static GetObjectSetStorageInputBuilder builder() {
        return new GetObjectSetStorageInputBuilder();
    }

    public static final class GetObjectSetStorageInputBuilder {
        private String bucket;
        private String objectSetName;

        private GetObjectSetStorageInputBuilder() {
        }

        public GetObjectSetStorageInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetStorageInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public GetObjectSetStorageInput build() {
            GetObjectSetStorageInput input = new GetObjectSetStorageInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
