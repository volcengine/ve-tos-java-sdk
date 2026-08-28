package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteConvertWorkflowInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteConvertWorkflowInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public static DeleteConvertWorkflowInputBuilder builder() {
        return new DeleteConvertWorkflowInputBuilder();
    }

    public static class DeleteConvertWorkflowInputBuilder {
        private String bucket;
        private String id;

        public DeleteConvertWorkflowInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteConvertWorkflowInputBuilder id(String id) {
            this.id = id;
            return this;
        }

        public DeleteConvertWorkflowInput build() {
            DeleteConvertWorkflowInput input = new DeleteConvertWorkflowInput();
            input.setBucket(bucket);
            return input;
        }
    }

    @Override
    public String toString() {
        return "DeleteConvertWorkflowInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}