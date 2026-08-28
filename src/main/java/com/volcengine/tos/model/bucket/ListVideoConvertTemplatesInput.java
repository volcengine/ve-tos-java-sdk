package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListVideoConvertTemplatesInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public ListVideoConvertTemplatesInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public static ListVideoConvertTemplatesInputBuilder builder() {
        return new ListVideoConvertTemplatesInputBuilder();
    }

    public static class ListVideoConvertTemplatesInputBuilder {
        private String bucket;

        public ListVideoConvertTemplatesInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListVideoConvertTemplatesInput build() {
            return new ListVideoConvertTemplatesInput().setBucket(bucket);
        }
    }
}