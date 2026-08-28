package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Delogo {
    @JsonProperty("Pos")
    private String pos;

    @JsonProperty("LocMode")
    private String locMode;

    @JsonProperty("SizeMode")
    private String sizeMode;

    @JsonProperty("Dx")
    private Integer dx;

    @JsonProperty("Dy")
    private Integer dy;

    @JsonProperty("Width")
    private Integer width;

    @JsonProperty("Height")
    private Integer height;

    @JsonProperty("StartTime")
    private Long startTime;

    @JsonProperty("EndTime")
    private Long endTime;

    public String getPos() {
        return pos;
    }

    public Delogo setPos(String pos) {
        this.pos = pos;
        return this;
    }

    public String getLocMode() {
        return locMode;
    }

    public Delogo setLocMode(String locMode) {
        this.locMode = locMode;
        return this;
    }

    public String getSizeMode() {
        return sizeMode;
    }

    public Delogo setSizeMode(String sizeMode) {
        this.sizeMode = sizeMode;
        return this;
    }

    public Integer getDx() {
        return dx;
    }

    public Delogo setDx(Integer dx) {
        this.dx = dx;
        return this;
    }

    public Integer getDy() {
        return dy;
    }

    public Delogo setDy(Integer dy) {
        this.dy = dy;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Delogo setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public Delogo setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Delogo setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Delogo setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public static DelogoBuilder builder() {
        return new DelogoBuilder();
    }

    public static class DelogoBuilder {
        private String pos;
        private String locMode;
        private String sizeMode;
        private Integer dx;
        private Integer dy;
        private Integer width;
        private Integer height;
        private Long startTime;
        private Long endTime;

        public DelogoBuilder pos(String pos) {
            this.pos = pos;
            return this;
        }

        public DelogoBuilder locMode(String locMode) {
            this.locMode = locMode;
            return this;
        }

        public DelogoBuilder sizeMode(String sizeMode) {
            this.sizeMode = sizeMode;
            return this;
        }

        public DelogoBuilder dx(Integer dx) {
            this.dx = dx;
            return this;
        }

        public DelogoBuilder dy(Integer dy) {
            this.dy = dy;
            return this;
        }

        public DelogoBuilder width(Integer width) {
            this.width = width;
            return this;
        }

        public DelogoBuilder height(Integer height) {
            this.height = height;
            return this;
        }

        public DelogoBuilder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public DelogoBuilder endTime(Long endTime) {
            this.endTime = endTime;
            return this;
        }

        public Delogo build() {
            Delogo delogo = new Delogo();
            delogo.setPos(pos);
            delogo.setLocMode(locMode);
            delogo.setSizeMode(sizeMode);
            delogo.setDx(dx);
            delogo.setDy(dy);
            delogo.setWidth(width);
            delogo.setHeight(height);
            delogo.setStartTime(startTime);
            delogo.setEndTime(endTime);
            return delogo;
        }
    }
}
