package com.volcengine.tos.comm.common;

public enum MetadataDirectiveType {
    METADATA_DIRECTIVE_COPY("COPY"),
    METADATA_DIRECTIVE_REPLACE("REPLACE");
    private String type;
    private MetadataDirectiveType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
