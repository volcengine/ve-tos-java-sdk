package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassType;

public class NonCurrentVersionAccessTimeTransition {
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;

    @JsonProperty("NoncurrentDays")
    private int nonCurrentDays;

    public NonCurrentVersionAccessTimeTransition() {
    }

    public NonCurrentVersionAccessTimeTransition(StorageClassType storageClass, int nonCurrentDays) {
        this.storageClass = storageClass;
        this.nonCurrentDays = nonCurrentDays;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public NonCurrentVersionAccessTimeTransition setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public int getNonCurrentDays() {
        return nonCurrentDays;
    }

    public NonCurrentVersionAccessTimeTransition setNonCurrentDays(int nonCurrentDays) {
        this.nonCurrentDays = nonCurrentDays;
        return this;
    }

    @Override
    public String toString() {
        return "NonCurrentVersionAccessTimeTransition{" +
                "storageClass=" + storageClass +
                ", nonCurrentDays=" + nonCurrentDays +
                '}';
    }
}