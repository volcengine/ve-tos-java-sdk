package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteAudioConvertTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonIgnore
    private String id;
    
    public String getBucket() {
        return bucket;
    }
    
    public DeleteAudioConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    
    public String getId() {
        return id;
    }
    
    public DeleteAudioConvertTemplateInput setId(String id) {
        this.id = id;
        return this;
    }
    
    @Override
    public String toString() {
        return "DeleteAudioConvertTemplateInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
    
    public static DeleteAudioConvertTemplateInputBuilder builder() {
        return new DeleteAudioConvertTemplateInputBuilder();
    }
    
    public static class DeleteAudioConvertTemplateInputBuilder {
        private String bucket;
        private String id;
        
        public DeleteAudioConvertTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }
        
        public DeleteAudioConvertTemplateInputBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public DeleteAudioConvertTemplateInput build() {
            DeleteAudioConvertTemplateInput input = new DeleteAudioConvertTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }
}