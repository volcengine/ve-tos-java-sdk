package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListedCommonPrefix {
    @JsonProperty("Prefix")
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "ListedCommonPrefix{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
