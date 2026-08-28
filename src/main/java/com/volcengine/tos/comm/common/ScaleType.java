package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ScaleType {
    CROP("crop"),
    STRETCH("stretch"),
    FILL("fill"),
    FIT("fit");

    private final String value;

    ScaleType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ScaleType fromValue(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        for (ScaleType type : ScaleType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ScaleType: " + value);
    }
}
