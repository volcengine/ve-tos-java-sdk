package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StorageClassType {
    /**
     * 标准存储
     */
    STORAGE_CLASS_STANDARD("STANDARD"),

    /**
     * 低频存储
     */
    STORAGE_CLASS_IA("IA"),

    /**
     * 归档闪回存储
     */
    STORAGE_CLASS_ARCHIVE_FR("ARCHIVE_FR");

    private String storageClass;
    private StorageClassType(String type) {
        this.storageClass = type;
    }

    @JsonValue
    public String getStorageClass() {
        return storageClass;
    }

    public StorageClassType setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return storageClass;
    }
}
