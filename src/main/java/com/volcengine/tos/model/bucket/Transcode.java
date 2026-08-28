package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transcode {
    @JsonProperty("TimeInterval")
    private TimeInterval timeInterval;
    
    @JsonProperty("Container")
    private Container container;
    
    @JsonProperty("Video")
    private Video video;
    
    @JsonProperty("Audio")
    private Audio audio;
    
    @JsonProperty("Options")
    private TranscodeConfigOptions options;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public Transcode setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

    public Container getContainer() {
        return container;
    }

    public Transcode setContainer(Container container) {
        this.container = container;
        return this;
    }

    public Video getVideo() {
        return video;
    }

    public Transcode setVideo(Video video) {
        this.video = video;
        return this;
    }

    public Audio getAudio() {
        return audio;
    }

    public Transcode setAudio(Audio audio) {
        this.audio = audio;
        return this;
    }

    public TranscodeConfigOptions getOptions() {
        return options;
    }

    public Transcode setOptions(TranscodeConfigOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "Transcode{" +
                "timeInterval=" + timeInterval +
                ", container=" + container +
                ", video=" + video +
                ", audio=" + audio +
                ", options=" + options +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TimeInterval timeInterval;
        private Container container;
        private Video video;
        private Audio audio;
        private TranscodeConfigOptions options;

        public Builder timeInterval(TimeInterval timeInterval) {
            this.timeInterval = timeInterval;
            return this;
        }

        public Builder container(Container container) {
            this.container = container;
            return this;
        }

        public Builder video(Video video) {
            this.video = video;
            return this;
        }

        public Builder audio(Audio audio) {
            this.audio = audio;
            return this;
        }

        public Builder options(TranscodeConfigOptions options) {
            this.options = options;
            return this;
        }

        public Transcode build() {
            Transcode transcode = new Transcode();
            transcode.setTimeInterval(timeInterval);
            transcode.setContainer(container);
            transcode.setVideo(video);
            transcode.setAudio(audio);
            transcode.setOptions(options);
            return transcode;
        }
    }
}