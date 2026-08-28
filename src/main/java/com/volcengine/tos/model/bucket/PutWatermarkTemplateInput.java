package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutWatermarkTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("WatermarkConfig")
    private Watermark watermarkConfig;

    public String getBucket() {
        return bucket;
    }

    public PutWatermarkTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getName() {
        return name;
    }

    public PutWatermarkTemplateInput setName(String name) {
        this.name = name;
        return this;
    }

    public Watermark getWatermarkConfig() {
        return watermarkConfig;
    }

    public PutWatermarkTemplateInput setWatermarkConfig(Watermark watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
        return this;
    }

    @Override
    public String toString() {
        return "PutWatermarkTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", name='" + name + '\'' +
                ", watermarkConfig=" + watermarkConfig +
                '}';
    }

    public static PutWatermarkTemplateInputBuilder builder() {
        return new PutWatermarkTemplateInputBuilder();
    }

    public static class PutWatermarkTemplateInputBuilder {
        private String bucket;
        private String name;
        private Watermark watermarkConfig;

        public PutWatermarkTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutWatermarkTemplateInputBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PutWatermarkTemplateInputBuilder watermarkConfig(Watermark watermarkConfig) {
            this.watermarkConfig = watermarkConfig;
            return this;
        }

        public PutWatermarkTemplateInput build() {
            PutWatermarkTemplateInput input = new PutWatermarkTemplateInput();
            input.setBucket(bucket);
            input.setName(name);
            input.setWatermarkConfig(watermarkConfig);
            return input;
        }
    }
}
