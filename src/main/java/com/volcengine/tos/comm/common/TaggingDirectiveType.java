package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaggingDirectiveType {
    TaggingDirectiveCopy("Copy"),
    TaggingDirectiveReplace("Replace");

    private String type;

    private TaggingDirectiveType(String type) {
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
