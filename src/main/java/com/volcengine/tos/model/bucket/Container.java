package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ContainerFormatType;

public class Container {
    @JsonProperty("Format")
    private ContainerFormatType format;
    
    @JsonProperty("ClipConfig")
    private ClipConfig clipConfig;

    public ContainerFormatType getFormat() {
        return format;
    }

    public Container setFormat(ContainerFormatType format) {
        this.format = format;
        return this;
    }

    public ClipConfig getClipConfig() {
        return clipConfig;
    }

    public Container setClipConfig(ClipConfig clipConfig) {
        this.clipConfig = clipConfig;
        return this;
    }

    @Override
    public String toString() {
        return "Container{" +
                "format=" + format +
                ", clipConfig=" + clipConfig +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContainerFormatType format;
        private ClipConfig clipConfig;

        public Builder format(ContainerFormatType format) {
            this.format = format;
            return this;
        }

        public Builder clipConfig(ClipConfig clipConfig) {
            this.clipConfig = clipConfig;
            return this;
        }

        public Container build() {
            Container container = new Container();
            container.setFormat(format);
            container.setClipConfig(clipConfig);
            return container;
        }
    }
}