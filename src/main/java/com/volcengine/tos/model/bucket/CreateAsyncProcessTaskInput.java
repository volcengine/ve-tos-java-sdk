package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class CreateAsyncProcessTaskInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String key;

    @JsonIgnore
    private String asyncProcess;

    public String getBucket() {
        return bucket;
    }

    public CreateAsyncProcessTaskInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CreateAsyncProcessTaskInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getAsyncProcess() {
        return asyncProcess;
    }

    public CreateAsyncProcessTaskInput setAsyncProcess(String asyncProcess) {
        this.asyncProcess = asyncProcess;
        return this;
    }

    public static CreateAsyncProcessTaskInputBuilder builder() {
        return new CreateAsyncProcessTaskInputBuilder();
    }

    public static class CreateAsyncProcessTaskInputBuilder {
        private String bucket;
        private String key;
        private String asyncProcess;

        public CreateAsyncProcessTaskInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateAsyncProcessTaskInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CreateAsyncProcessTaskInputBuilder asyncProcess(String asyncProcess) {
            this.asyncProcess = asyncProcess;
            return this;
        }

        public CreateAsyncProcessTaskInput build() {
            CreateAsyncProcessTaskInput input = new CreateAsyncProcessTaskInput();
            input.setBucket(bucket);
            input.setKey(key);
            input.setAsyncProcess(asyncProcess);
            return input;
        }
    }
}
