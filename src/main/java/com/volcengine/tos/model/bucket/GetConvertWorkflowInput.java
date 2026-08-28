package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetConvertWorkflowInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetConvertWorkflowInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public static GetConvertWorkflowInputBuilder builder() {
        return new GetConvertWorkflowInputBuilder();
    }

    public static class GetConvertWorkflowInputBuilder {
        private String bucket;

        public GetConvertWorkflowInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetConvertWorkflowInput build() {
            GetConvertWorkflowInput input = new GetConvertWorkflowInput();
            input.setBucket(bucket);
            return input;
        }
    }

    @Override
    public String toString() {
        return "GetConvertWorkflowInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }
}