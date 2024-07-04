package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryFormatType {
    INVENTORY_FORMAT_CSV("CSV");

    private String type;

    private InventoryFormatType(String type) {
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
