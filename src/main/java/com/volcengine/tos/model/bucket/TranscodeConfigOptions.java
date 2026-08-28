package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TranscodeConfigOptions {
    @JsonProperty("AIGCMetadata")
    private AIGCMetadata aigcMetadata;

    public AIGCMetadata getAigcMetadata() {
        return aigcMetadata;
    }

    public TranscodeConfigOptions setAigcMetadata(AIGCMetadata aigcMetadata) {
        this.aigcMetadata = aigcMetadata;
        return this;
    }

    @Override
    public String toString() {
        return "TranscodeConfigOptions{" +
                "aigcMetadata=" + aigcMetadata +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AIGCMetadata aigcMetadata;

        public Builder aigcMetadata(AIGCMetadata aigcMetadata) {
            this.aigcMetadata = aigcMetadata;
            return this;
        }

        public TranscodeConfigOptions build() {
            TranscodeConfigOptions options = new TranscodeConfigOptions();
            options.setAigcMetadata(aigcMetadata);
            return options;
        }
    }
}