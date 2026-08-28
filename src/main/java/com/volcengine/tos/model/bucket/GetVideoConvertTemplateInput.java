package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetVideoConvertTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String id;

    public String getBucket() {
        return bucket;
    }

    public GetVideoConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetVideoConvertTemplateInput setId(String id) {
        this.id = id;
        return this;
    }

    public static GetVideoConvertTemplateInputBuilder builder() {
        return new GetVideoConvertTemplateInputBuilder();
    }

    public static class GetVideoConvertTemplateInputBuilder {
        private String bucket;
        private String id;

        public GetVideoConvertTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetVideoConvertTemplateInputBuilder id(String id) {
            this.id = id;
            return this;
        }

        public GetVideoConvertTemplateInput build() {
            GetVideoConvertTemplateInput input = new GetVideoConvertTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }

    @Override
    public String toString() {
        return "GetVideoConvertTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}