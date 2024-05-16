package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.volcengine.tos.internal.util.StringUtils;

public enum TierType {
    TIER_STANDARD("Standard"),
    TIER_EXPEDITED("Expedited"),
    TIER_BULK("Bulk"),
    @JsonEnumDefaultValue
    RESTORE_TIER_TYPE_UNKNOWN("Unknown");

    private String type;
    private TierType(String type) {
        this.type = type;
    }

    public static TierType parse(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        if ("Standard".equals(input)) {
            return TIER_STANDARD;
        }
        if ("Expedited".equals(input)) {
            return TIER_EXPEDITED;
        }
        if ("Bulk".equals(input)) {
            return TIER_BULK;
        }
        return null;
    }

    @Override
    public String toString() {
        return type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public TierType setType(String type) {
        this.type = type;
        return this;
    }
}
