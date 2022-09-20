package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CannedType {
    CANNED_ALL_USERS("AllUsers"),
    CANNED_AUTHENTICATED_USERS("AuthenticatedUsers"),
    CANNED_LOG_DELIVERY("LogDelivery");
    private String canned;
    private CannedType(String canned) {
        this.canned = canned;
    }

    @Override
    public String toString() {
        return canned;
    }

    @JsonValue
    public String getCanned() {
        return canned;
    }

    public CannedType canned(String canned) {
        this.canned = canned;
        return this;
    }
}
