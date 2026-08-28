package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListAudioConvertTemplatesInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public ListAudioConvertTemplatesInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "ListAudioConvertTemplatesInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bucket;
        private String id;

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public ListAudioConvertTemplatesInput build() {
            ListAudioConvertTemplatesInput input = new ListAudioConvertTemplatesInput();
            input.setBucket(bucket);
            return input;
        }
    }
}