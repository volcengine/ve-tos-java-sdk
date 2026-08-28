package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassType;

public class AccessTimeTransition {
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("Days")
    private int days;

    public AccessTimeTransition() {
    }

    public AccessTimeTransition(StorageClassType storageClass, int days) {
        this.storageClass = storageClass;
        this.days = days;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public AccessTimeTransition setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public int getDays() {
        return days;
    }

    public AccessTimeTransition setDays(int days) {
        this.days = days;
        return this;
    }

    @Override
    public String toString() {
        return "AccessTimeTransition{" +
                "storageClass=" + storageClass +
                ", day=" + days +
                '}';
    }
}