package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetObjectSetQuotaByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetObjectSetQuotaByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetQuotaByTagInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetObjectSetQuotaByTagInputBuilder builder() {
        return new GetObjectSetQuotaByTagInputBuilder();
    }

    public static final class GetObjectSetQuotaByTagInputBuilder {
        private String bucket;

        private GetObjectSetQuotaByTagInputBuilder() {
        }

        public GetObjectSetQuotaByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectSetQuotaByTagInput build() {
            GetObjectSetQuotaByTagInput input = new GetObjectSetQuotaByTagInput();
            input.setBucket(bucket);
            return input;
        }
    }
}
