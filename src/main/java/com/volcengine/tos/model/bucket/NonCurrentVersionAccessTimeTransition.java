package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.StorageClassType;

public class NonCurrentVersionAccessTimeTransition {
    private StorageClassType storageClassType;
    private int nonCurrentDays;

    public NonCurrentVersionAccessTimeTransition() {
    }

    public NonCurrentVersionAccessTimeTransition(StorageClassType storageClassType, int nonCurrentDays) {
        this.storageClassType = storageClassType;
        this.nonCurrentDays = nonCurrentDays;
    }

    public StorageClassType getStorageClassType() {
        return storageClassType;
    }

    public NonCurrentVersionAccessTimeTransition setStorageClassType(StorageClassType storageClassType) {
        this.storageClassType = storageClassType;
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
                "storageClassType=" + storageClassType +
                ", nonCurrentDays=" + nonCurrentDays +
                '}';
    }
}