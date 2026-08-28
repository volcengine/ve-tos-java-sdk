package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WorkflowExtFilter {
    @JsonProperty("AudioExts")
    private List<String> audioExts;
    
    @JsonProperty("VideoExts")
    private List<String> videoExts;

    public List<String> getAudioExts() {
        return audioExts;
    }

    public WorkflowExtFilter setAudioExts(List<String> audioExts) {
        this.audioExts = audioExts;
        return this;
    }

    public List<String> getVideoExts() {
        return videoExts;
    }

    public WorkflowExtFilter setVideoExts(List<String> videoExts) {
        this.videoExts = videoExts;
        return this;
    }

    public static WorkflowExtFilterBuilder builder() {
        return new WorkflowExtFilterBuilder();
    }

    public static class WorkflowExtFilterBuilder {
        private List<String> audioExts;
        private List<String> videoExts;

        public WorkflowExtFilterBuilder audioExts(List<String> audioExts) {
            this.audioExts = audioExts;
            return this;
        }

        public WorkflowExtFilterBuilder videoExts(List<String> videoExts) {
            this.videoExts = videoExts;
            return this;
        }

        public WorkflowExtFilter build() {
            WorkflowExtFilter f = new WorkflowExtFilter();
            f.setAudioExts(audioExts);
            f.setVideoExts(videoExts);
            return f;
        }
    }

    @Override
    public String toString() {
        return "WorkflowExtFilter{" +
                "audioExts=" + audioExts +
                ", videoExts=" + videoExts +
                '}';
    }
}
