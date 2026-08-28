package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TranscodeConfig {
    @JsonProperty("TemplateID")
    private String templateID;

    @JsonProperty("Transcode")
    private Transcode transcode;

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

    public static TranscodeConfigBuilder builder() {
        return new TranscodeConfigBuilder();
    }

    public static class TranscodeConfigBuilder {
        private String templateID;
        private Transcode transcode;

        public TranscodeConfigBuilder templateID(String templateID) {
            this.templateID = templateID;
            return this;
        }

        public TranscodeConfigBuilder transcode(Transcode transcode) {
            this.transcode = transcode;
            return this;
        }

        public TranscodeConfig build() {
            TranscodeConfig config = new TranscodeConfig();
            config.setTemplateID(templateID);
            config.setTranscode(transcode);
            return config;
        }
    }
}