package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType {
    DATA_TYPE_FLOAT32("float32");

    private final String value;

    DataType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DataType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (DataType type : DataType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}