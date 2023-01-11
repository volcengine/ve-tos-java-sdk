package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class ListedOwner {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;

    public String getId() {
        return id;
    }

    public ListedOwner setId(String id) {
        this.id = id;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ListedOwner setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public String toString() {
        return "ListedOwner{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
