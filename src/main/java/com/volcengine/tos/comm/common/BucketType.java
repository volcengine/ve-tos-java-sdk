package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BucketType {
    // flat namespace
    BUCKET_TYPE_FNS("fns"),
    // hierarchical namespace
    BUCKET_TYPE_HNS("hns");

    private String type;

    private BucketType(String type) {
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

    public static BucketType parse(String input) {
        if ("fns".equals(input)) {
            return BUCKET_TYPE_FNS;
        }

        if ("hns".equals(input)) {
            return BUCKET_TYPE_HNS;
        }
        return null;
    }
}
