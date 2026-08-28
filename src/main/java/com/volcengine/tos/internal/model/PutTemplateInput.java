package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.bucket.AudioConvertConfig;
import com.volcengine.tos.model.bucket.Transcode;
import com.volcengine.tos.model.bucket.Watermark;

public class PutTemplateInput {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;

    @JsonProperty("TranscodeConfig")
    private Transcode transcodeConfig;

    @JsonProperty("WatermarkConfig")
    private Watermark watermarkConfig;

    @JsonProperty("Tag")
    private String tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public void setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
    }

    public Transcode getTranscodeConfig() {
        return transcodeConfig;
    }

    public void setTranscodeConfig(Transcode transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
    }

    public Watermark getWatermarkConfig() {
        return watermarkConfig;
    }

    public void setWatermarkConfig(Watermark watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "PutTemplateInput{" +
                "name='" + name + '\'' +
                ", audioConvertConfig=" + audioConvertConfig +
                ", transcodeConfig=" + transcodeConfig +
                ", watermarkConfig=" + watermarkConfig +
                ", tag='" + tag + '\'' +
                '}';
    }
}
