package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobStateType {
    SUBMITTED("Submitted"),
    RUNNING("Running"),
    SUCCESS("Success"),
    FAILED("Failed"),
    PAUSED("Paused"),
    CANCELED("Canceled");

    private final String value;

    JobStateType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static JobStateType fromValue(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        for (JobStateType type : JobStateType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown JobStateType: " + value);
    }
}
