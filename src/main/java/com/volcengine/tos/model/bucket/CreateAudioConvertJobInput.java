package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class CreateAudioConvertJobInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonProperty("Input")
    private ConvertJobInput input;
    
    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;
    
    @JsonProperty("Output")
    private ConvertJobOutput output;

    public CreateAudioConvertJobInput() {}

    public CreateAudioConvertJobInput(String bucket, ConvertJobInput input, AudioConvertConfig audioConvertConfig, ConvertJobOutput output) {
        this.bucket = bucket;
        this.input = input;
        this.audioConvertConfig = audioConvertConfig;
        this.output = output;
    }

    public String getBucket() {
        return bucket;
    }

    public CreateAudioConvertJobInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public CreateAudioConvertJobInput setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public CreateAudioConvertJobInput setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public CreateAudioConvertJobInput setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    public static CreateAudioConvertJobInputBuilder builder() {
        return new CreateAudioConvertJobInputBuilder();
    }

    public static class CreateAudioConvertJobInputBuilder {
        private String bucket;
        private ConvertJobInput input;
        private AudioConvertConfig audioConvertConfig;
        private ConvertJobOutput output;

        public CreateAudioConvertJobInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateAudioConvertJobInputBuilder input(ConvertJobInput input) {
            this.input = input;
            return this;
        }

        public CreateAudioConvertJobInputBuilder audioConvertConfig(AudioConvertConfig audioConvertConfig) {
            this.audioConvertConfig = audioConvertConfig;
            return this;
        }

        public CreateAudioConvertJobInputBuilder output(ConvertJobOutput output) {
            this.output = output;
            return this;
        }

        public CreateAudioConvertJobInput build() {
            return new CreateAudioConvertJobInput(bucket, input, audioConvertConfig, output);
        }
    }
}