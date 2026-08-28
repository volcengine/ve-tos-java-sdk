package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ScaleType;

public class Animation {
    @JsonProperty("Width")
    private int width;

    @JsonProperty("Height")
    private int height;

    @JsonProperty("Fps")
    private int fps;

    @JsonProperty("FrameInterval")
    private int frameInterval;

    @JsonProperty("FrameNum")
    private int frameNum;

    @JsonProperty("ScaleType")
    private ScaleType scaleType;

    public int getWidth() {
        return width;
    }

    public Animation setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Animation setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getFps() {
        return fps;
    }

    public Animation setFps(int fps) {
        this.fps = fps;
        return this;
    }

    public int getFrameInterval() {
        return frameInterval;
    }

    public Animation setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
        return this;
    }

    public int getFrameNum() {
        return frameNum;
    }

    public Animation setFrameNum(int frameNum) {
        this.frameNum = frameNum;
        return this;
    }

    public ScaleType getScaleType() {
        return scaleType;
    }

    public Animation setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public static AnimationBuilder builder() {
        return new AnimationBuilder();
    }

    public static class AnimationBuilder {
        private int width;
        private int height;
        private int fps;
        private int frameInterval;
        private int frameNum;
        private ScaleType scaleType;

        public AnimationBuilder width(int width) {
            this.width = width;
            return this;
        }

        public AnimationBuilder height(int height) {
            this.height = height;
            return this;
        }

        public AnimationBuilder fps(int fps) {
            this.fps = fps;
            return this;
        }

        public AnimationBuilder frameInterval(int frameInterval) {
            this.frameInterval = frameInterval;
            return this;
        }

        public AnimationBuilder frameNum(int frameNum) {
            this.frameNum = frameNum;
            return this;
        }

        public AnimationBuilder scaleType(ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Animation build() {
            Animation animation = new Animation();
            animation.setWidth(width);
            animation.setHeight(height);
            animation.setFps(fps);
            animation.setFrameInterval(frameInterval);
            animation.setFrameNum(frameNum);
            animation.setScaleType(scaleType);
            return animation;
        }
    }
}
