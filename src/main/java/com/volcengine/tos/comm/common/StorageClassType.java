package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
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
    STORAGE_CLASS_ARCHIVE_FR("ARCHIVE_FR"),

    /**
     * 智能分层
     */
    STORAGE_CLASS_INTELLIGENT_TIERING("INTELLIGENT_TIERING"),

    /**
     * 冷归档
     */
    STORAGE_CLASS_COLD_ARCHIVE("COLD_ARCHIVE"),
    /**
     * 归档
     */
    STORAGE_CLASS_ARCHIVE("ARCHIVE"),
    /**
     * 深度冷归档
     */
    STORAGE_CLASS_DEEP_COLD_ARCHIVE("DEEP_COLD_ARCHIVE"),

    @JsonEnumDefaultValue
    STORAGE_CLASS_UNKNOWN("UNKNOWN");

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
