package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WatermarkText {
    @JsonProperty("FontSize")
    private Integer fontSize;

    @JsonProperty("FontType")
    private String fontType;

    @JsonProperty("FontColor")
    private String fontColor;

    @JsonProperty("Transparency")
    private Integer transparency;

    @JsonProperty("Text")
    private String text;

    public Integer getFontSize() {
        return fontSize;
    }

    public WatermarkText setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public String getFontType() {
        return fontType;
    }

    public WatermarkText setFontType(String fontType) {
        this.fontType = fontType;
        return this;
    }

    public String getFontColor() {
        return fontColor;
    }

    public WatermarkText setFontColor(String fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public Integer getTransparency() {
        return transparency;
    }

    public WatermarkText setTransparency(Integer transparency) {
        this.transparency = transparency;
        return this;
    }

    public String getText() {
        return text;
    }

    public WatermarkText setText(String text) {
        this.text = text;
        return this;
    }

    public static WatermarkTextBuilder builder() {
        return new WatermarkTextBuilder();
    }

    public static class WatermarkTextBuilder {
        private Integer fontSize;
        private String fontType;
        private String fontColor;
        private Integer transparency;
        private String text;

        public WatermarkTextBuilder fontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public WatermarkTextBuilder fontType(String fontType) {
            this.fontType = fontType;
            return this;
        }

        public WatermarkTextBuilder fontColor(String fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public WatermarkTextBuilder transparency(Integer transparency) {
            this.transparency = transparency;
            return this;
        }

        public WatermarkTextBuilder text(String text) {
            this.text = text;
            return this;
        }

        public WatermarkText build() {
            WatermarkText text = new WatermarkText();
            text.setFontSize(fontSize);
            text.setFontType(fontType);
            text.setFontColor(fontColor);
            text.setTransparency(transparency);
            text.setText(this.text);
            return text;
        }
    }
}
