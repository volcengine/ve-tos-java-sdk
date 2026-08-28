package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutAudioConvertTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;

    public String getBucket() {
        return bucket;
    }

    public PutAudioConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getName() {
        return name;
    }

    public PutAudioConvertTemplateInput setName(String name) {
        this.name = name;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public PutAudioConvertTemplateInput setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    @Override
    public String toString() {
        return "PutAudioConvertTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", name='" + name + '\'' +
                ", audioConvertConfig=" + audioConvertConfig +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bucket;
        private String name;
        private AudioConvertConfig audioConvertConfig;

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder audioConvertConfig(AudioConvertConfig audioConvertConfig) {
            this.audioConvertConfig = audioConvertConfig;
            return this;
        }

        public PutAudioConvertTemplateInput build() {
            PutAudioConvertTemplateInput input = new PutAudioConvertTemplateInput();
            input.setBucket(bucket);
            input.setName(name);
            input.setAudioConvertConfig(audioConvertConfig);
            return input;
        }
    }
}