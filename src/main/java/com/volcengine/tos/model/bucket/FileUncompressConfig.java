package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUncompressConfig {
    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("PrefixReplaced")
    private Integer prefixReplaced;

    public String getPrefix() {
        return prefix;
    }

    public FileUncompressConfig setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Integer getPrefixReplaced() {
        return prefixReplaced;
    }

    public FileUncompressConfig setPrefixReplaced(Integer prefixReplaced) {
        this.prefixReplaced = prefixReplaced;
        return this;
    }

    @Override
    public String toString() {
        return "FileUncompressConfig{" +
                "prefix='" + prefix + '\'' +
                ", prefixReplaced=" + prefixReplaced +
                '}';
    }
}
