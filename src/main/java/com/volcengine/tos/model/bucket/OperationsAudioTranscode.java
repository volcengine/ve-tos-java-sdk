package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationsAudioTranscode {
    @JsonProperty("OperationID")
    private String operationID;
    
    @JsonProperty("TemplateID")
    private String templateID;
    
    @JsonProperty("Output")
    private ConvertJobOutput output;

    public String getOperationID() {
        return operationID;
    }

    public OperationsAudioTranscode setOperationID(String operationID) {
        this.operationID = operationID;
        return this;
    }

    public String getTemplateID() {
        return templateID;
    }

    public OperationsAudioTranscode setTemplateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public OperationsAudioTranscode setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    public static OperationsAudioTranscodeBuilder builder() {
        return new OperationsAudioTranscodeBuilder();
    }

    public static class OperationsAudioTranscodeBuilder {
        private String operationID;
        private String templateID;
        private ConvertJobOutput output;

        public OperationsAudioTranscodeBuilder operationID(String operationID) {
            this.operationID = operationID;
            return this;
        }

        public OperationsAudioTranscodeBuilder templateID(String templateID) {
            this.templateID = templateID;
            return this;
        }

        public OperationsAudioTranscodeBuilder output(ConvertJobOutput output) {
            this.output = output;
            return this;
        }

        public OperationsAudioTranscode build() {
            OperationsAudioTranscode o = new OperationsAudioTranscode();
            o.setOperationID(operationID);
            o.setTemplateID(templateID);
            o.setOutput(output);
            return o;
        }
    }

    @Override
    public String toString() {
        return "OperationsAudioTranscode{" +
                "operationID='" + operationID + '\'' +
                ", templateID='" + templateID + '\'' +
                ", output=" + output +
                '}';
    }
}
