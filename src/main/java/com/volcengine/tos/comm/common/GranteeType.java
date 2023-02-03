package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GranteeType {
    GRANTEE_GROUP("Group"),
    GRANTEE_USER("CanonicalUser"),
    @JsonEnumDefaultValue
    GRANTEE_UNKNOWN("Unknown");
    private String type;
    private GranteeType(String type) {
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

    public GranteeType type(String type) {
        this.type = type;
        return this;
    }
}
