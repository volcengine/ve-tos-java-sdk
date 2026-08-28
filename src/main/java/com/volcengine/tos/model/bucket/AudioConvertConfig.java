package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.AudioContainerFormatType;

public class AudioConvertConfig {
    @JsonProperty("TimeInterval")
    private TimeInterval timeInterval;
    
    @JsonProperty("ContainerFormat")
    private AudioContainerFormatType containerFormat;
    
    @JsonProperty("BitRate")
    private int bitRate;
    
    @JsonProperty("BitRateOpt")
    private int bitRateOpt;
    
    @JsonProperty("SampleFormat")
    private String sampleFormat;
    
    @JsonProperty("SampleRate")
    private int sampleRate;
    
    @JsonProperty("Channels")
    private int channels;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public AudioConvertConfig setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

    public AudioContainerFormatType getContainerFormat() {
        return containerFormat;
    }

    public AudioConvertConfig setContainerFormat(AudioContainerFormatType containerFormat) {
        this.containerFormat = containerFormat;
        return this;
    }

    public int getBitRate() {
        return bitRate;
    }

    public AudioConvertConfig setBitRate(int bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public int getBitRateOpt() {
        return bitRateOpt;
    }

    public AudioConvertConfig setBitRateOpt(int bitRateOpt) {
        this.bitRateOpt = bitRateOpt;
        return this;
    }

    public String getSampleFormat() {
        return sampleFormat;
    }

    public AudioConvertConfig setSampleFormat(String sampleFormat) {
        this.sampleFormat = sampleFormat;
        return this;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public AudioConvertConfig setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public int getChannels() {
        return channels;
    }

    public AudioConvertConfig setChannels(int channels) {
        this.channels = channels;
        return this;
    }

    @Override
    public String toString() {
        return "AudioConvertConfig{" +
                "timeInterval=" + timeInterval +
                ", containerFormat=" + containerFormat +
                ", bitRate=" + bitRate +
                ", bitRateOpt=" + bitRateOpt +
                ", sampleFormat='" + sampleFormat + '\'' +
                ", sampleRate=" + sampleRate +
                ", channels=" + channels +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TimeInterval timeInterval;
        private AudioContainerFormatType containerFormat;
        private int bitRate;
        private int bitRateOpt;
        private String sampleFormat;
        private int sampleRate;
        private int channels;

        public Builder timeInterval(TimeInterval timeInterval) {
            this.timeInterval = timeInterval;
            return this;
        }

        public Builder containerFormat(AudioContainerFormatType containerFormat) {
            this.containerFormat = containerFormat;
            return this;
        }

        public Builder bitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
        }

        public Builder bitRateOpt(int bitRateOpt) {
            this.bitRateOpt = bitRateOpt;
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

        public AudioConvertConfig build() {
            AudioConvertConfig config = new AudioConvertConfig();
            config.setTimeInterval(timeInterval);
            config.setContainerFormat(containerFormat);
            config.setBitRate(bitRate);
            config.setBitRateOpt(bitRateOpt);
            config.setSampleFormat(sampleFormat);
            config.setSampleRate(sampleRate);
            config.setChannels(channels);
            return config;
        }
    }
}