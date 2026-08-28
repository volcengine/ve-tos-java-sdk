package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video {
    @JsonProperty("Codec")
    private String codec;
    
    @JsonProperty("Width")
    private int width;
    
    @JsonProperty("Height")
    private int height;
    
    @JsonProperty("Crf")
    private int crf;
    
    @JsonProperty("PixFmt")
    private String pixFmt;
    
    @JsonProperty("BitRate")
    private int bitRate;
    
    @JsonProperty("Fps")
    private int fps;
    
    @JsonProperty("Remove")
    private Boolean remove;

    public String getCodec() {
        return codec;
    }

    public Video setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Video setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Video setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getCrf() {
        return crf;
    }

    public Video setCrf(int crf) {
        this.crf = crf;
        return this;
    }

    public String getPixFmt() {
        return pixFmt;
    }

    public Video setPixFmt(String pixFmt) {
        this.pixFmt = pixFmt;
        return this;
    }

    public int getBitRate() {
        return bitRate;
    }

    public Video setBitRate(int bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public int getFps() {
        return fps;
    }

    public Video setFps(int fps) {
        this.fps = fps;
        return this;
    }

    public Boolean getRemove() {
        return remove;
    }

    public Video setRemove(Boolean remove) {
        this.remove = remove;
        return this;
    }

    @Override
    public String toString() {
        return "Video{" +
                "codec='" + codec + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", crf=" + crf +
                ", pixFmt='" + pixFmt + '\'' +
                ", bitRate=" + bitRate +
                ", fps=" + fps +
                ", remove=" + remove +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String codec;
        private int width;
        private int height;
        private int crf;
        private String pixFmt;
        private int bitRate;
        private int fps;
        private Boolean remove;

        public Builder codec(String codec) {
            this.codec = codec;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder crf(int crf) {
            this.crf = crf;
            return this;
        }

        public Builder pixFmt(String pixFmt) {
            this.pixFmt = pixFmt;
            return this;
        }

        public Builder bitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
        }

        public Builder fps(int fps) {
            this.fps = fps;
            return this;
        }

        public Builder remove(Boolean remove) {
            this.remove = remove;
            return this;
        }

        public Video build() {
            Video video = new Video();
            video.setCodec(codec);
            video.setWidth(width);
            video.setHeight(height);
            video.setCrf(crf);
            video.setPixFmt(pixFmt);
            video.setBitRate(bitRate);
            video.setFps(fps);
            video.setRemove(remove);
            return video;
        }
    }
}