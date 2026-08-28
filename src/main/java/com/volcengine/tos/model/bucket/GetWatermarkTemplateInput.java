package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetWatermarkTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String id;

    public String getBucket() {
        return bucket;
    }

    public GetWatermarkTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetWatermarkTemplateInput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "GetWatermarkTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static GetWatermarkTemplateInputBuilder builder() {
        return new GetWatermarkTemplateInputBuilder();
    }

    public static class GetWatermarkTemplateInputBuilder {
        private String bucket;
        private String id;

        public GetWatermarkTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetWatermarkTemplateInputBuilder id(String id) {
            this.id = id;
            return this;
        }

        public GetWatermarkTemplateInput build() {
            GetWatermarkTemplateInput input = new GetWatermarkTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }
}
