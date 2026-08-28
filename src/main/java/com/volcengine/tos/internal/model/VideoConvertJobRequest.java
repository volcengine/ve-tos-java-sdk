package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.bucket.ConvertJobInput;
import com.volcengine.tos.model.bucket.ConvertJobOutput;
import com.volcengine.tos.model.bucket.TranscodeConfig;

public class VideoConvertJobRequest {
    @JsonProperty("Tag")
    private String tag;

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

    public VideoConvertJobRequest setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public VideoConvertJobRequest setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public VideoConvertJobRequest setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public TranscodeConfig getTranscodeConfig() {
        return transcodeConfig;
    }

    public VideoConvertJobRequest setTranscodeConfig(TranscodeConfig transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public VideoConvertJobRequest setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }
}