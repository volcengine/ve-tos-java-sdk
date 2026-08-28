package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetQuotaInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetQuotaInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetQuotaInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetQuotaInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                '}';
    }

    public static GetObjectSetQuotaInputBuilder builder() {
        return new GetObjectSetQuotaInputBuilder();
    }

    public static final class GetObjectSetQuotaInputBuilder {
        private String bucket;
        private String objectSetName;

        private GetObjectSetQuotaInputBuilder() {
        }

        public GetObjectSetQuotaInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetQuotaInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public GetObjectSetQuotaInput build() {
            GetObjectSetQuotaInput input = new GetObjectSetQuotaInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            return input;
        }
    }
}
