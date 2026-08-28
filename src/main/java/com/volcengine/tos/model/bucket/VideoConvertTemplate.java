package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoConvertTemplate {
    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("TranscodeConfig")
    private Transcode transcodeConfig;

    public String getTag() {
        return tag;
    }

    public VideoConvertTemplate setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public VideoConvertTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public VideoConvertTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public Transcode getTranscodeConfig() {
        return transcodeConfig;
    }

    public VideoConvertTemplate setTranscodeConfig(Transcode transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }

    @Override
    public String toString() {
        return "VideoConvertTemplate{" +
                "tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", transcodeConfig=" + transcodeConfig +
                '}';
    }
}