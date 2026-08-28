package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatermarkImage {
    @JsonProperty("Url")
    private String url;

    @JsonProperty("Mode")
    private String mode;

    @JsonProperty("Width")
    private Integer width;

    @JsonProperty("Height")
    private Integer height;

    @JsonProperty("Transparency")
    private Integer transparency;

    @JsonProperty("Background")
    private Boolean background;

    public String getUrl() {
        return url;
    }

    public WatermarkImage setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public WatermarkImage setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public WatermarkImage setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public WatermarkImage setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getTransparency() {
        return transparency;
    }

    public WatermarkImage setTransparency(Integer transparency) {
        this.transparency = transparency;
        return this;
    }

    public Boolean getBackground() {
        return background;
    }

    public WatermarkImage setBackground(Boolean background) {
        this.background = background;
        return this;
    }

    public static WatermarkImageBuilder builder() {
        return new WatermarkImageBuilder();
    }

    public static class WatermarkImageBuilder {
        private String url;
        private String mode;
        private Integer width;
        private Integer height;
        private Integer transparency;
        private Boolean background;

        public WatermarkImageBuilder url(String url) {
            this.url = url;
            return this;
        }

        public WatermarkImageBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public WatermarkImageBuilder width(Integer width) {
            this.width = width;
            return this;
        }

        public WatermarkImageBuilder height(Integer height) {
            this.height = height;
            return this;
        }

        public WatermarkImageBuilder transparency(Integer transparency) {
            this.transparency = transparency;
            return this;
        }

        public WatermarkImageBuilder background(Boolean background) {
            this.background = background;
            return this;
        }

        public WatermarkImage build() {
            WatermarkImage image = new WatermarkImage();
            image.setUrl(url);
            image.setMode(mode);
            image.setWidth(width);
            image.setHeight(height);
            image.setTransparency(transparency);
            image.setBackground(background);
            return image;
        }
    }
}
