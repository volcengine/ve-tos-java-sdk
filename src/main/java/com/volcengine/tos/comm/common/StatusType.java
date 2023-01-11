package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {
    STATUS_ENABLED("Enabled"),
    STATUS_DISABLED("Disabled");

    private String type;
    private StatusType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public StatusType setType(String type) {
        this.type = type;
        return this;
    }
}
