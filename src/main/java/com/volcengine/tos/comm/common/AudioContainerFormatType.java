package com.volcengine.tos.comm.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AudioContainerFormatType {
    WAV("wav"),
    MP3("mp3"),
    AAC("aac"),
    FLAC("flac"),
    OGA("oga"),
    AC3("ac3"),
    OPUS("opus");

    private final String value;

    AudioContainerFormatType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AudioContainerFormatType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (AudioContainerFormatType type : AudioContainerFormatType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown AudioContainerFormatType: " + value);
    }
}