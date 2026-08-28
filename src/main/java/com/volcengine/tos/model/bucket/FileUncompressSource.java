package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUncompressSource {
    @JsonProperty("Object")
    private String object;

    public String getObject() {
        return object;
    }

    public FileUncompressSource setObject(String object) {
        this.object = object;
        return this;
    }

    @Override
    public String toString() {
        return "FileUncompressSource{" +
                "object='" + object + '\'' +
                '}';
    }
}
