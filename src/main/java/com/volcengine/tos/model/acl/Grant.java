package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Grant {
    @JsonProperty("Grantee")
    private Grantee grantee;
    @JsonProperty("Permission")
    private String permission;

    public Grantee getGrantee() {
        return grantee;
    }

    public Grant setGrantee(Grantee grantee) {
        this.grantee = grantee;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public Grant setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "grantee=" + grantee +
                ", permission='" + permission + '\'' +
                '}';
    }
}
