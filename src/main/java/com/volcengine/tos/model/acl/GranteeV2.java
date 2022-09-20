package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.CannedType;
import com.volcengine.tos.comm.common.GranteeType;

public class GranteeV2 {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Type")
    private GranteeType type;
    @JsonProperty("Canned")
    private CannedType canned;

    public String getId() {
        return id;
    }

    public GranteeV2 setId(String id) {
        this.id = id;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GranteeV2 setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public GranteeType getType() {
        return type;
    }

    public GranteeV2 setType(GranteeType type) {
        this.type = type;
        return this;
    }

    public CannedType getCanned() {
        return canned;
    }

    public GranteeV2 setCanned(CannedType canned) {
        this.canned = canned;
        return this;
    }

    @Override
    public String toString() {
        return "GranteeV2{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", uri=" + canned +
                '}';
    }

    public static GranteeV2Builder builder() {
        return new GranteeV2Builder();
    }

    public static final class GranteeV2Builder {
        private String id;
        private String displayName;
        private GranteeType type;
        private CannedType canned;

        private GranteeV2Builder() {
        }

        public GranteeV2Builder id(String id) {
            this.id = id;
            return this;
        }

        public GranteeV2Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public GranteeV2Builder type(GranteeType type) {
            this.type = type;
            return this;
        }

        public GranteeV2Builder canned(CannedType uri) {
            this.canned = uri;
            return this;
        }

        public GranteeV2 build() {
            GranteeV2 granteeV2 = new GranteeV2();
            granteeV2.type = this.type;
            granteeV2.canned = this.canned;
            granteeV2.displayName = this.displayName;
            granteeV2.id = this.id;
            return granteeV2;
        }
    }
}
