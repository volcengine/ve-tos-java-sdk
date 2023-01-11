package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VersioningStatusType {
    VERSIONING_STATUS_ENABLED("Enabled"),

    VERSIONING_STATUS_SUSPENDED("Suspended");

    private String versioningStatusType;
    private VersioningStatusType(String type) {
        this.versioningStatusType = type;
    }

    @JsonValue
    public String getVersioningStatusType() {
        return versioningStatusType;
    }

    public VersioningStatusType setVersioningStatusType(String versioningStatusType) {
        this.versioningStatusType = versioningStatusType;
        return this;
    }

    @Override
    public String toString() {
        return versioningStatusType;
    }
}
