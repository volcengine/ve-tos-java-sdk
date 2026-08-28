package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Audio {
    @JsonProperty("Codec")
    private String codec;
    
    @JsonProperty("BitRate")
    private int bitRate;
    
    @JsonProperty("SampleFormat")
    private String sampleFormat;
    
    @JsonProperty("SampleRate")
    private int sampleRate;
    
    @JsonProperty("Channels")
    private int channels;
    
    @JsonProperty("Remove")
    private Boolean remove;

    public String getCodec() {
        return codec;
    }

    public Audio setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    public int getBitRate() {
        return bitRate;
    }

    public Audio setBitRate(int bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public String getSampleFormat() {
        return sampleFormat;
    }

    public Audio setSampleFormat(String sampleFormat) {
        this.sampleFormat = sampleFormat;
        return this;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public Audio setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public int getChannels() {
        return channels;
    }

    public Audio setChannels(int channels) {
        this.channels = channels;
        return this;
    }

    public Boolean getRemove() {
        return remove;
    }

    public Audio setRemove(Boolean remove) {
        this.remove = remove;
        return this;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "codec='" + codec + '\'' +
                ", bitRate=" + bitRate +
                ", sampleFormat='" + sampleFormat + '\'' +
                ", sampleRate=" + sampleRate +
                ", channels=" + channels +
                ", remove=" + remove +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String codec;
        private int bitRate;
        private String sampleFormat;
        private int sampleRate;
        private int channels;
        private Boolean remove;

        public Builder codec(String codec) {
            this.codec = codec;
            return this;
        }

        public Builder bitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
        }

        public Builder sampleFormat(String sampleFormat) {
            this.sampleFormat = sampleFormat;
            return this;
        }

        public Builder sampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        public Builder remove(Boolean remove) {
            this.remove = remove;
            return this;
        }

        public Audio build() {
            Audio audio = new Audio();
            audio.setCodec(codec);
            audio.setBitRate(bitRate);
            audio.setSampleFormat(sampleFormat);
            audio.setSampleRate(sampleRate);
            audio.setChannels(channels);
            audio.setRemove(remove);
            return audio;
        }
    }
}