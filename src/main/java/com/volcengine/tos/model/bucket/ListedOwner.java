package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListedOwner {
    @JsonProperty("ID")
    private String id;

    @Override
    public String toString() {
        return "ListedOwner{" +
                "id='" + id + '\'' +
                '}';
    }
}
