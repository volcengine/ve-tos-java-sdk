package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.bucket.AudioConvertConfig;
import com.volcengine.tos.model.bucket.ConvertJobInput;
import com.volcengine.tos.model.bucket.ConvertJobOutput;

public class AudioConvertJobRequest {
    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("Input")
    private ConvertJobInput input;

    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;

    @JsonProperty("Output")
    private ConvertJobOutput output;

    public String getTag() {
        return tag;
    }

    public AudioConvertJobRequest setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public AudioConvertJobRequest setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public AudioConvertJobRequest setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public AudioConvertJobRequest setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }
}