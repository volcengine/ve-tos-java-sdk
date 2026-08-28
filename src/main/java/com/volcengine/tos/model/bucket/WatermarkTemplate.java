package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatermarkTemplate {
    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("WatermarkConfig")
    private Watermark watermarkConfig;

    public String getTag() {
        return tag;
    }

    public WatermarkTemplate setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public WatermarkTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WatermarkTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public Watermark getWatermarkConfig() {
        return watermarkConfig;
    }

    public WatermarkTemplate setWatermarkConfig(Watermark watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
        return this;
    }

    @Override
    public String toString() {
        return "WatermarkTemplate{" +
                "tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", watermarkConfig=" + watermarkConfig +
                '}';
    }
}
