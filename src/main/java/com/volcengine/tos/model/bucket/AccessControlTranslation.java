package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessControlTranslation {
    @JsonProperty("Owner")
    private String owner;

    public String getOwner() {
        return owner;
    }

    public AccessControlTranslation setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "AccessControlTranslation{" +
                "owner='" + owner + '\'' +
                '}';
    }
}
