package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;
import java.util.List;

public class PutConvertWorkflowInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonProperty("Rules")
    private List<ConvertWorkflowRule> rules;

    public String getBucket() {
        return bucket;
    }

    public PutConvertWorkflowInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<ConvertWorkflowRule> getRules() {
        return rules;
    }

    public PutConvertWorkflowInput setRules(List<ConvertWorkflowRule> rules) {
        this.rules = rules;
        return this;
    }

    public static PutConvertWorkflowInputBuilder builder() {
        return new PutConvertWorkflowInputBuilder();
    }

    public static class PutConvertWorkflowInputBuilder {
        private String bucket;
        private List<ConvertWorkflowRule> rules;

        public PutConvertWorkflowInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutConvertWorkflowInputBuilder rules(List<ConvertWorkflowRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutConvertWorkflowInput build() {
            PutConvertWorkflowInput input = new PutConvertWorkflowInput();
            input.setBucket(bucket);
            input.setRules(rules);
            return input;
        }
    }

    @Override
    public String toString() {
        return "PutConvertWorkflowInput{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                '}';
    }
}