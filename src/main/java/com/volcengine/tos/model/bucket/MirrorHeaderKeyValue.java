package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MirrorHeaderKeyValue {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;

    public String getKey() {
        return key;
    }

    public MirrorHeaderKeyValue setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MirrorHeaderKeyValue setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "MirrorHeaderKeyValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MirrorHeaderKeyValue that = (MirrorHeaderKeyValue) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
