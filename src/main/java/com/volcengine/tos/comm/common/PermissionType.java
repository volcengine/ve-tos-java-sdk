package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType {
    PERMISSION_READ("READ"),
    PERMISSION_WRITE("WRITE"),
    PERMISSION_READ_ACP("READ_ACP"),
    PERMISSION_WRITE_ACP("WRITE_ACP"),
    PERMISSION_FULL_CONTROL("FULL_CONTROL"),
    @JsonEnumDefaultValue
    PERMISSION_UNKNOWN("UNKNOWN");
    private String permission;
    private PermissionType(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return permission;
    }

    @JsonValue
    public String getPermission() {
        return permission;
    }

    public PermissionType permission(String permission) {
        this.permission = permission;
        return this;
    }
}
