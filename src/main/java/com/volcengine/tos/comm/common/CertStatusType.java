package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CertStatusType {
    CERT_STATUS_BOUND("CertBound"),

    CERT_STATUS_UNBOUND("CertUnbound"),

    CERT_STATUS_EXPIRED("CertExpired");

    private String certStatusType;
    private CertStatusType(String type) {
        this.certStatusType = type;
    }

    @JsonValue
    public String getCertStatusType() {
        return certStatusType;
    }

    public CertStatusType setCertStatusType(String certStatusType) {
        this.certStatusType = certStatusType;
        return this;
    }

    @Override
    public String toString() {
        return certStatusType;
    }
}
