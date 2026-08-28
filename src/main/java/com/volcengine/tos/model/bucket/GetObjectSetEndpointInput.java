package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetEndpointInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetEndpointInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetEndpointInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetEndpointInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static GetObjectSetEndpointInputBuilder builder() {
        return new GetObjectSetEndpointInputBuilder();
    }

    public static final class GetObjectSetEndpointInputBuilder {
        private String bucket;
        private String objectSetName;

        private GetObjectSetEndpointInputBuilder() {
        }

        public GetObjectSetEndpointInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetEndpointInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public GetObjectSetEndpointInput build() {
            GetObjectSetEndpointInput input = new GetObjectSetEndpointInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
