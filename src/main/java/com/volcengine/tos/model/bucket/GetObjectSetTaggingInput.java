package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetTaggingInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetTaggingInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static GetObjectSetTaggingInputBuilder builder() {
        return new GetObjectSetTaggingInputBuilder();
    }

    public static final class GetObjectSetTaggingInputBuilder {
        private String bucket;
        private String objectSetName;

        private GetObjectSetTaggingInputBuilder() {
        }

        public GetObjectSetTaggingInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetTaggingInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public GetObjectSetTaggingInput build() {
            GetObjectSetTaggingInput input = new GetObjectSetTaggingInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
