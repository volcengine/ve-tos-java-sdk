package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ListedCommonPrefix {
    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("LastModified")
    private Date lastModified;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public ListedCommonPrefix setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Override
    public String toString() {
        return "ListedCommonPrefix{" +
                "prefix='" + prefix + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
