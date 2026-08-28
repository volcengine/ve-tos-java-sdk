package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileCompressConfig {
    @JsonProperty("Format")
    private String format;

    @JsonProperty("Flatten")
    private Integer flatten;

    public String getFormat() {
        return format;
    }

    public FileCompressConfig setFormat(String format) {
        this.format = format;
        return this;
    }

    public Integer getFlatten() {
        return flatten;
    }

    public FileCompressConfig setFlatten(Integer flatten) {
        this.flatten = flatten;
        return this;
    }

    @Override
    public String toString() {
        return "FileCompressConfig{" +
                "format='" + format + '\'' +
                ", flatten=" + flatten +
                '}';
    }
}
