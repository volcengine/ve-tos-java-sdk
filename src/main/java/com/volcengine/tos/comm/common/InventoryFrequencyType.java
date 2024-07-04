package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryFrequencyType {
    INVENTORY_FREQUENCY_TYPE_DAILY("Daily"),
    INVENTORY_FREQUENCY_TYPE_WEEKLY("Weekly");

    private String type;

    private InventoryFrequencyType(String type) {
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
}
