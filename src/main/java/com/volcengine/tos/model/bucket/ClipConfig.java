package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClipConfig {
    @JsonProperty("Duration")
    private int duration;

    public int getDuration() {
        return duration;
    }

    public ClipConfig setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public String toString() {
        return "ClipConfig{" +
                "duration=" + duration +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int duration;

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public ClipConfig build() {
            ClipConfig clipConfig = new ClipConfig();
            clipConfig.setDuration(duration);
            return clipConfig;
        }
    }
}