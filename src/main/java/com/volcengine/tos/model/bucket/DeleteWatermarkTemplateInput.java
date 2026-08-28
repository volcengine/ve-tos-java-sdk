package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteWatermarkTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String id;

    public String getBucket() {
        return bucket;
    }

    public DeleteWatermarkTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public DeleteWatermarkTemplateInput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteWatermarkTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static DeleteWatermarkTemplateInputBuilder builder() {
        return new DeleteWatermarkTemplateInputBuilder();
    }

    public static class DeleteWatermarkTemplateInputBuilder {
        private String bucket;
        private String id;

        public DeleteWatermarkTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteWatermarkTemplateInputBuilder id(String id) {
            this.id = id;
            return this;
        }

        public DeleteWatermarkTemplateInput build() {
            DeleteWatermarkTemplateInput input = new DeleteWatermarkTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }
}
