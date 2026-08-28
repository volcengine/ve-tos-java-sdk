package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Vector {
    @JsonProperty("key")
    private String key;

    @JsonProperty("data")
    private VectorData data;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    public String getKey() {
        return key;
    }

    public Vector setKey(String key) {
        this.key = key;
        return this;
    }

    public VectorData getData() {
        return data;
    }

    public Vector setData(VectorData data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Vector setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String key;
        private VectorData data;
        private Map<String, Object> metadata;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder data(VectorData data) {
            this.data = data;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Vector build() {
            Vector vector = new Vector();
            vector.setKey(key);
            vector.setData(data);
            vector.setMetadata(metadata);
            return vector;
        }
    }

    @Override
    public String toString() {
        return "Vector{" +
                "key='" + key + '\'' +
                ", data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}