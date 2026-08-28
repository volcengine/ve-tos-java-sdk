package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Watermark {
    @JsonProperty("Type")
    private String type;

    @JsonProperty("Pos")
    private String pos;

    @JsonProperty("LocMode")
    private String locMode;

    @JsonProperty("Dx")
    private Integer dx;

    @JsonProperty("Dy")
    private Integer dy;

    @JsonProperty("StartTime")
    private Integer startTime;

    @JsonProperty("EndTime")
    private Integer endTime;

    @JsonProperty("Text")
    private WatermarkText text;

    @JsonProperty("Image")
    private WatermarkImage image;

    public String getType() {
        return type;
    }

    public Watermark setType(String type) {
        this.type = type;
        return this;
    }

    public String getPos() {
        return pos;
    }

    public Watermark setPos(String pos) {
        this.pos = pos;
        return this;
    }

    public String getLocMode() {
        return locMode;
    }

    public Watermark setLocMode(String locMode) {
        this.locMode = locMode;
        return this;
    }

    public Integer getDx() {
        return dx;
    }

    public Watermark setDx(Integer dx) {
        this.dx = dx;
        return this;
    }

    public Integer getDy() {
        return dy;
    }

    public Watermark setDy(Integer dy) {
        this.dy = dy;
        return this;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public Watermark setStartTime(Integer startTime) {
        this.startTime = startTime;
        return this;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public Watermark setEndTime(Integer endTime) {
        this.endTime = endTime;
        return this;
    }

    public WatermarkText getText() {
        return text;
    }

    public Watermark setText(WatermarkText text) {
        this.text = text;
        return this;
    }

    public WatermarkImage getImage() {
        return image;
    }

    public Watermark setImage(WatermarkImage image) {
        this.image = image;
        return this;
    }

    public static WatermarkBuilder builder() {
        return new WatermarkBuilder();
    }

    public static class WatermarkBuilder {
        private String type;
        private String pos;
        private String locMode;
        private Integer dx;
        private Integer dy;
        private Integer startTime;
        private Integer endTime;
        private WatermarkText text;
        private WatermarkImage image;

        public WatermarkBuilder type(String type) {
            this.type = type;
            return this;
        }

        public WatermarkBuilder pos(String pos) {
            this.pos = pos;
            return this;
        }

        public WatermarkBuilder locMode(String locMode) {
            this.locMode = locMode;
            return this;
        }

        public WatermarkBuilder dx(Integer dx) {
            this.dx = dx;
            return this;
        }

        public WatermarkBuilder dy(Integer dy) {
            this.dy = dy;
            return this;
        }

        public WatermarkBuilder startTime(Integer startTime) {
            this.startTime = startTime;
            return this;
        }

        public WatermarkBuilder endTime(Integer endTime) {
            this.endTime = endTime;
            return this;
        }

        public WatermarkBuilder text(WatermarkText text) {
            this.text = text;
            return this;
        }

        public WatermarkBuilder image(WatermarkImage image) {
            this.image = image;
            return this;
        }

        public Watermark build() {
            Watermark watermark = new Watermark();
            watermark.setType(type);
            watermark.setPos(pos);
            watermark.setLocMode(locMode);
            watermark.setDx(dx);
            watermark.setDy(dy);
            watermark.setStartTime(startTime);
            watermark.setEndTime(endTime);
            watermark.setText(text);
            watermark.setImage(image);
            return watermark;
        }
    }
}
