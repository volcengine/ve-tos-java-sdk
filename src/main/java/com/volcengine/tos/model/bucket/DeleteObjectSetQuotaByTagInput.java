package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteObjectSetQuotaByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteObjectSetQuotaByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectSetQuotaByTagInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteObjectSetQuotaByTagInputBuilder builder() {
        return new DeleteObjectSetQuotaByTagInputBuilder();
    }

    public static final class DeleteObjectSetQuotaByTagInputBuilder {
        private String bucket;

        private DeleteObjectSetQuotaByTagInputBuilder() {
        }

        public DeleteObjectSetQuotaByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteObjectSetQuotaByTagInput build() {
            DeleteObjectSetQuotaByTagInput input = new DeleteObjectSetQuotaByTagInput();
            input.setBucket(bucket);
            return input;
        }
    }
}
