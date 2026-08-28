package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.StorageClassType;

public class AccessTimeTransition {
    private StorageClassType storageClassType;
    private int day;

    public AccessTimeTransition() {
    }

    public AccessTimeTransition(StorageClassType storageClassType, int day) {
        this.storageClassType = storageClassType;
        this.day = day;
    }

    public StorageClassType getStorageClassType() {
        return storageClassType;
    }

    public AccessTimeTransition setStorageClassType(StorageClassType storageClassType) {
        this.storageClassType = storageClassType;
        return this;
    }

    public int getDay() {
        return day;
    }

    public AccessTimeTransition setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public String toString() {
        return "AccessTimeTransition{" +
                "storageClassType=" + storageClassType +
                ", day=" + day +
                '}';
    }
}