package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileCompressSource {
    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("KeyConfig")
    private List<FileCompressSourceKey> keyConfig;

    public String getPrefix() {
        return prefix;
    }

    public FileCompressSource setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public List<FileCompressSourceKey> getKeyConfig() {
        return keyConfig;
    }

    public FileCompressSource setKeyConfig(List<FileCompressSourceKey> keyConfig) {
        this.keyConfig = keyConfig;
        return this;
    }

    @Override
    public String toString() {
        return "FileCompressSource{" +
                "prefix='" + prefix + '\'' +
                ", keyConfig=" + keyConfig +
                '}';
    }
}
