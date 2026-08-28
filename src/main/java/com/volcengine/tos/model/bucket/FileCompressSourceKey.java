package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileCompressSourceKey {
    @JsonProperty("Key")
    private String key;

    public String getKey() {
        return key;
    }

    public FileCompressSourceKey setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public String toString() {
        return "FileCompressSourceKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
