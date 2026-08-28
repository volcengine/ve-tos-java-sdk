package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteVideoConvertTemplateInput extends GenericInput {
    private String bucket;
    private String id;

    public String getBucket() {
        return bucket;
    }

    public DeleteVideoConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public DeleteVideoConvertTemplateInput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteVideoConvertTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bucket;
        private String id;

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public DeleteVideoConvertTemplateInput build() {
            DeleteVideoConvertTemplateInput input = new DeleteVideoConvertTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }
}