package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class ListedOwner {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;

    @Override
    public String toString() {
        return "ListedOwner{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
