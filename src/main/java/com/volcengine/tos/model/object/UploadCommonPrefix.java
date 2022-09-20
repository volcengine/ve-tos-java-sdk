package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class UploadCommonPrefix {
    @JsonProperty("Prefix")
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public UploadCommonPrefix setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String toString() {
        return "UploadCommonPrefix{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
