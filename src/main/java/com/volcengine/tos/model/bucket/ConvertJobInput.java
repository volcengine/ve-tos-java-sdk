package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertJobInput {
    @JsonProperty("Object")
    private String object;
    
    public String getObject() {
        return object;
    }
    
    public ConvertJobInput setObject(String object) {
        this.object = object;
        return this;
    }

    public static ConvertJobInputBuilder builder() {
        return new ConvertJobInputBuilder();
    }

    public static class ConvertJobInputBuilder {
        private String object;

        public ConvertJobInputBuilder object(String object) {
            this.object = object;
            return this;
        }

        public ConvertJobInput build() {
            ConvertJobInput input = new ConvertJobInput();
            input.setObject(object);
            return input;
        }
    }
}
