package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DistanceMetricType {
    DISTANCE_METRIC_EUCLIDEAN("euclidean"),
    DISTANCE_METRIC_COSINE("cosine");

    private final String value;

    DistanceMetricType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DistanceMetricType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (DistanceMetricType type : DistanceMetricType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}