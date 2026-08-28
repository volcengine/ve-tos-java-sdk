package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationsTranscode {
    @JsonProperty("OperationID")
    private String operationID;
    
    @JsonProperty("TemplateID")
    private String templateID;
    
    @JsonProperty("Output")
    private ConvertJobOutput output;

    public String getOperationID() {
        return operationID;
    }

    public OperationsTranscode setOperationID(String operationID) {
        this.operationID = operationID;
        return this;
    }

    public String getTemplateID() {
        return templateID;
    }

    public OperationsTranscode setTemplateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public OperationsTranscode setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    public static OperationsTranscodeBuilder builder() {
        return new OperationsTranscodeBuilder();
    }

    public static class OperationsTranscodeBuilder {
        private String operationID;
        private String templateID;
        private ConvertJobOutput output;

        public OperationsTranscodeBuilder operationID(String operationID) {
            this.operationID = operationID;
            return this;
        }

        public OperationsTranscodeBuilder templateID(String templateID) {
            this.templateID = templateID;
            return this;
        }

        public OperationsTranscodeBuilder output(ConvertJobOutput output) {
            this.output = output;
            return this;
        }

        public OperationsTranscode build() {
            OperationsTranscode o = new OperationsTranscode();
            o.setOperationID(operationID);
            o.setTemplateID(templateID);
            o.setOutput(output);
            return o;
        }
    }

    @Override
    public String toString() {
        return "OperationsTranscode{" +
                "operationID='" + operationID + '\'' +
                ", templateID='" + templateID + '\'' +
                ", output=" + output +
                '}';
    }
}
