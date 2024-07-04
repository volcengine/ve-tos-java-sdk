package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryIncludedObjType {
    INVENTORY_INCLUDED_OBJ_TYPE_ALL("All"),
    INVENTORY_INCLUDED_OBJ_TYPE_CURRENT("Current");
    private String type;

    private InventoryIncludedObjType(String type) {
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
