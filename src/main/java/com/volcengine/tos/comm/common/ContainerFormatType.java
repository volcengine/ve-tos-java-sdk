package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContainerFormatType {
    MP4("mp4"),
    TS("ts"),
    MP3("mp3"),
    AAC("aac"),
    FLAC("flac"),
    M4A("m4a"),
    OGA("oga"),
    WAV("wav"),
    AC3("ac3"),
    OPUS("opus"),
    HLS("hls");

    private final String value;

    ContainerFormatType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ContainerFormatType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ContainerFormatType type : ContainerFormatType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ContainerFormatType: " + value);
    }
}
