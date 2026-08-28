package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutVideoConvertTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("TranscodeConfig")
    private Transcode transcodeConfig;

    public String getBucket() {
        return bucket;
    }

    public PutVideoConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getName() {
        return name;
    }

    public PutVideoConvertTemplateInput setName(String name) {
        this.name = name;
        return this;
    }

    public Transcode getTranscodeConfig() {
        return transcodeConfig;
    }

    public PutVideoConvertTemplateInput setTranscodeConfig(Transcode transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }

    @Override
    public String toString() {
        return "PutVideoConvertTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", name='" + name + '\'' +
                ", transcodeConfig=" + transcodeConfig +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bucket;
        private String name;
        private Transcode transcodeConfig;

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder transcodeConfig(Transcode transcodeConfig) {
            this.transcodeConfig = transcodeConfig;
            return this;
        }

        public PutVideoConvertTemplateInput build() {
            PutVideoConvertTemplateInput input = new PutVideoConvertTemplateInput();
            input.setBucket(bucket);
            input.setName(name);
            input.setTranscodeConfig(transcodeConfig);
            return input;
        }
    }
}