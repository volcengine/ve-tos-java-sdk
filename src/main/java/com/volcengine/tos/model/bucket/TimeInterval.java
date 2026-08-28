package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeInterval {
    @JsonProperty("Start")
    private int start;
    
    @JsonProperty("Duration")
    private int duration;

    public int getStart() {
        return start;
    }

    public TimeInterval setStart(int start) {
        this.start = start;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public TimeInterval setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "start=" + start +
                ", duration=" + duration +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int start;
        private int duration;

        public Builder start(int start) {
            this.start = start;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public TimeInterval build() {
            TimeInterval timeInterval = new TimeInterval();
            timeInterval.setStart(start);
            timeInterval.setDuration(duration);
            return timeInterval;
        }
    }
}