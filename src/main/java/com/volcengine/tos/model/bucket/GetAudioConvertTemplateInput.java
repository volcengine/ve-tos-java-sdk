package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetAudioConvertTemplateInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonIgnore
    private String id;

    public String getBucket() {
        return bucket;
    }

    public GetAudioConvertTemplateInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetAudioConvertTemplateInput setId(String id) {
        this.id = id;
        return this;
    }

    public static GetAudioConvertTemplateInputBuilder builder() {
        return new GetAudioConvertTemplateInputBuilder();
    }

    public static class GetAudioConvertTemplateInputBuilder {
        private String bucket;
        private String id;

        public GetAudioConvertTemplateInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetAudioConvertTemplateInputBuilder id(String id) {
            this.id = id;
            return this;
        }

        public GetAudioConvertTemplateInput build() {
            GetAudioConvertTemplateInput input = new GetAudioConvertTemplateInput();
            input.setBucket(bucket);
            input.setId(id);
            return input;
        }
    }
}