package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RedirectType {
    REDIRECT_MIRROR("Mirror"),
    REDIRECT_ASYNC("Async"),
    @JsonEnumDefaultValue
    REDIRECT_UNKNOWN("Unknown");

    private String type;
    private RedirectType(String type) {
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

    public RedirectType setType(String type) {
        this.type = type;
        return this;
    }
}
