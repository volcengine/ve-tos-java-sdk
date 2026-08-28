package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TranscodeConfig {
    @JsonProperty("TemplateID")
    private String templateID;

    @JsonProperty("Transcode")
    private Transcode transcode;

    @JsonProperty("Watermark")
    private List<Watermark> watermark;

    @JsonProperty("WatermarkTemplateID")
    private List<String> watermarkTemplateID;

    public String getTemplateID() {
        return templateID;
    }

    public TranscodeConfig setTemplateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public Transcode getTranscode() {
        return transcode;
    }

    public TranscodeConfig setTranscode(Transcode transcode) {
        this.transcode = transcode;
        return this;
    }

    public List<Watermark> getWatermark() {
        return watermark;
    }

    public TranscodeConfig setWatermark(List<Watermark> watermark) {
        this.watermark = watermark;
        return this;
    }

    public List<String> getWatermarkTemplateID() {
        return watermarkTemplateID;
    }

    public TranscodeConfig setWatermarkTemplateID(List<String> watermarkTemplateID) {
        this.watermarkTemplateID = watermarkTemplateID;
        return this;
    }

    public static TranscodeConfigBuilder builder() {
        return new TranscodeConfigBuilder();
    }

    public static class TranscodeConfigBuilder {
        private String templateID;
        private Transcode transcode;
        private List<Watermark> watermark;
        private List<String> watermarkTemplateID;

        public TranscodeConfigBuilder templateID(String templateID) {
            this.templateID = templateID;
            return this;
        }

        public TranscodeConfigBuilder transcode(Transcode transcode) {
            this.transcode = transcode;
            return this;
        }

        public TranscodeConfigBuilder watermark(List<Watermark> watermark) {
            this.watermark = watermark;
            return this;
        }

        public TranscodeConfigBuilder watermarkTemplateID(List<String> watermarkTemplateID) {
            this.watermarkTemplateID = watermarkTemplateID;
            return this;
        }

        public TranscodeConfig build() {
            TranscodeConfig config = new TranscodeConfig();
            config.setTemplateID(templateID);
            config.setTranscode(transcode);
            config.setWatermark(watermark);
            config.setWatermarkTemplateID(watermarkTemplateID);
            return config;
        }
    }
}
