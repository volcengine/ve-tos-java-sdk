package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthProtocolType {
    AUTH_PROTOCOL_TOS("tos"),

    AUTH_PROTOCOL_S3("s3"),

    @JsonEnumDefaultValue
    AUTH_PROTOCOL_UNKNOWN("unknown");

    private String authProtocolType;

    private AuthProtocolType(String type) {
        this.authProtocolType = type;
    }

    @JsonValue
    public String getAuthProtocolType() {
        return authProtocolType;
    }

    public AuthProtocolType setAuthProtocolType(String authProtocolType) {
        this.authProtocolType = authProtocolType;
        return this;
    }

    @Override
    public String toString() {
        return authProtocolType;
    }
}
