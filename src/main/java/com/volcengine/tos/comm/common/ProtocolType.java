package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProtocolType {
    PROTOCOL_HTTP("http"),

    PROTOCOL_HTTPS("https");

    private String protocolType;
    private ProtocolType(String type) {
        this.protocolType = type;
    }

    @JsonValue
    public String getProtocolType() {
        return protocolType;
    }

    public ProtocolType setProtocolType(String protocolType) {
        this.protocolType = protocolType;
        return this;
    }

    @Override
    public String toString() {
        return protocolType;
    }
}
