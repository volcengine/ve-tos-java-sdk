package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.PermissionType;

public class GrantV2 {
    @JsonProperty("Grantee")
    private GranteeV2 grantee;
    @JsonProperty("Permission")
    private PermissionType permission;

    public GranteeV2 getGrantee() {
        return grantee;
    }

    public GrantV2 setGrantee(GranteeV2 grantee) {
        this.grantee = grantee;
        return this;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public GrantV2 setPermission(PermissionType permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String toString() {
        return "GrantV2{" +
                "grantee=" + grantee +
                ", permission=" + permission +
                '}';
    }

    public static GrantV2Builder builder() {
        return new GrantV2Builder();
    }

    public static final class GrantV2Builder {
        private GranteeV2 grantee;
        private PermissionType permission;

        private GrantV2Builder() {
        }

        public GrantV2Builder grantee(GranteeV2 grantee) {
            this.grantee = grantee;
            return this;
        }

        public GrantV2Builder permission(PermissionType permission) {
            this.permission = permission;
            return this;
        }

        public GrantV2 build() {
            GrantV2 grantV2 = new GrantV2();
            grantV2.permission = this.permission;
            grantV2.grantee = this.grantee;
            return grantV2;
        }
    }
}
