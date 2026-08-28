package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class CreateVectorBucketInput extends GenericInput {
    @JsonProperty("vectorBucketName")
    private String vectorBucketName;

    public String getVectorBucketName() {
        return vectorBucketName;
    }

    public CreateVectorBucketInput setVectorBucketName(String vectorBucketName) {
        this.vectorBucketName = vectorBucketName;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vectorBucketName;

        public Builder vectorBucketName(String vectorBucketName) {
            this.vectorBucketName = vectorBucketName;
            return this;
        }

        public CreateVectorBucketInput build() {
            CreateVectorBucketInput input = new CreateVectorBucketInput();
            input.setVectorBucketName(vectorBucketName);
            return input;
        }
    }
}