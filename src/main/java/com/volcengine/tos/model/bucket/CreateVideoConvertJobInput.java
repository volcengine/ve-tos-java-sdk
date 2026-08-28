package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class CreateVideoConvertJobInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Input")
    private ConvertJobInput input;

    @JsonProperty("TranscodeConfig")
    private TranscodeConfig transcodeConfig;

    @JsonProperty("Output")
    private ConvertJobOutput output;

    @JsonProperty("Callback")
    private String callback;

    public String getCallback() {
        return callback;
    }

    public CreateVideoConvertJobInput setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public CreateVideoConvertJobInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public CreateVideoConvertJobInput setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public TranscodeConfig getTranscodeConfig() {
        return transcodeConfig;
    }

    public CreateVideoConvertJobInput setTranscodeConfig(TranscodeConfig transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public CreateVideoConvertJobInput setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    public static CreateVideoConvertJobInputBuilder builder() {
        return new CreateVideoConvertJobInputBuilder();
    }

    public static class CreateVideoConvertJobInputBuilder {
        private String bucket;
        private ConvertJobInput input;
        private TranscodeConfig transcodeConfig;
        private ConvertJobOutput output;
        private String callback;

        public CreateVideoConvertJobInputBuilder callback(String callback) {
            this.callback = callback;
            return this;
        }

        public CreateVideoConvertJobInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateVideoConvertJobInputBuilder input(ConvertJobInput input) {
            this.input = input;
            return this;
        }

        public CreateVideoConvertJobInputBuilder transcodeConfig(TranscodeConfig transcodeConfig) {
            this.transcodeConfig = transcodeConfig;
            return this;
        }

        public CreateVideoConvertJobInputBuilder output(ConvertJobOutput output) {
            this.output = output;
            return this;
        }

        public CreateVideoConvertJobInput build() {
            CreateVideoConvertJobInput input = new CreateVideoConvertJobInput();
            input.setBucket(bucket);
            input.setInput(this.input);
            input.setTranscodeConfig(transcodeConfig);
            input.setOutput(output);
            input.setCallback(callback);
            return input;
        }
    }
}
