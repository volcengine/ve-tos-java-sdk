package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StorageClassInheritDirectiveType {
    STORAGE_CLASS_ID_DESTINATION_BUCKET("DESTINATION_BUCKET"),

    STORAGE_CLASS_ID_SOURCE_OBJECT("SOURCE_OBJECT"),

    @JsonEnumDefaultValue
    STORAGE_CLASS_ID_UNKNOWN("UNKNOWN");

    private String storageClassInheritDirectiveType;
    private StorageClassInheritDirectiveType(String type) {
        this.storageClassInheritDirectiveType = type;
    }

    @JsonValue
    public String getStorageClassInheritDirectiveType() {
        return storageClassInheritDirectiveType;
    }

    public StorageClassInheritDirectiveType setStorageClassInheritDirectiveType(String storageClassInheritDirectiveType) {
        this.storageClassInheritDirectiveType = storageClassInheritDirectiveType;
        return this;
    }

    @Override
    public String toString() {
        return storageClassInheritDirectiveType;
    }
}
