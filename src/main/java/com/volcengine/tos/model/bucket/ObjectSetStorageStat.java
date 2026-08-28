package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectSetStorageStat {
    @JsonProperty("Storage")
    private String storage;

    @JsonProperty("ObjectCount")
    private Long objectCount;

    public String getStorage() {
        return storage;
    }

    public ObjectSetStorageStat setStorage(String storage) {
        this.storage = storage;
        return this;
    }

    public Long getObjectCount() {
        return objectCount;
    }

    public ObjectSetStorageStat setObjectCount(Long objectCount) {
        this.objectCount = objectCount;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetStorageStat{" +
                "storage='" + storage + '\'' +
                ", objectCount=" + objectCount +
                '}';
    }
}
