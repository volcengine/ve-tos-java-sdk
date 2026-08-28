package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WorkflowOperations {
    @JsonProperty("AudioTranscode")
    private List<OperationsAudioTranscode> audioTranscode;
    
    @JsonProperty("Transcode")
    private List<OperationsTranscode> transcode;

    public List<OperationsAudioTranscode> getAudioTranscode() {
        return audioTranscode;
    }

    public WorkflowOperations setAudioTranscode(List<OperationsAudioTranscode> audioTranscode) {
        this.audioTranscode = audioTranscode;
        return this;
    }

    public List<OperationsTranscode> getTranscode() {
        return transcode;
    }

    public WorkflowOperations setTranscode(List<OperationsTranscode> transcode) {
        this.transcode = transcode;
        return this;
    }

    public static WorkflowOperationsBuilder builder() {
        return new WorkflowOperationsBuilder();
    }

    public static class WorkflowOperationsBuilder {
        private List<OperationsAudioTranscode> audioTranscode;
        private List<OperationsTranscode> transcode;

        public WorkflowOperationsBuilder audioTranscode(List<OperationsAudioTranscode> audioTranscode) {
            this.audioTranscode = audioTranscode;
            return this;
        }

        public WorkflowOperationsBuilder transcode(List<OperationsTranscode> transcode) {
            this.transcode = transcode;
            return this;
        }

        public WorkflowOperations build() {
            WorkflowOperations o = new WorkflowOperations();
            o.setAudioTranscode(audioTranscode);
            o.setTranscode(transcode);
            return o;
        }
    }

    @Override
    public String toString() {
        return "WorkflowOperations{" +
                "audioTranscode=" + audioTranscode +
                ", transcode=" + transcode +
                '}';
    }
}
