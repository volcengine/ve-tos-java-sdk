package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DistanceVector {
    @JsonProperty("key")
    private String key;

    @JsonProperty("distance")
    private Float distance;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    public String getKey() {
        return key;
    }

    public DistanceVector setKey(String key) {
        this.key = key;
        return this;
    }

    public Float getDistance() {
        return distance;
    }

    public DistanceVector setDistance(Float distance) {
        this.distance = distance;
        return this;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public DistanceVector setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public String toString() {
        return "DistanceVector{" +
                "key='" + key + '\'' +
                ", distance=" + distance +
                ", metadata=" + metadata +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String key;
        private VectorData data;
        private Float distance;
        private Map<String, Object> metadata;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder data(VectorData data) {
            this.data = data;
            return this;
        }

        public Builder distance(Float distance) {
            this.distance = distance;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public DistanceVector build() {
            DistanceVector distanceVector = new DistanceVector();
            distanceVector.setKey(key);
            distanceVector.setDistance(distance);
            distanceVector.setMetadata(metadata);
            return distanceVector;
        }
    }
}