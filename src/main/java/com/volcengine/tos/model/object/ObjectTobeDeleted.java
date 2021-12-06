package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectTobeDeleted {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionID;

    public String getKey() {
        return key;
    }

    public ObjectTobeDeleted setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ObjectTobeDeleted setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectTobeDeleted{" +
                "key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
