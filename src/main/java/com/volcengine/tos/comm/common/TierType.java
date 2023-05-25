package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

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
