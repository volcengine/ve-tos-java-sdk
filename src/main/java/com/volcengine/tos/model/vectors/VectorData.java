package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VectorData {
    @JsonProperty("float32")
    private float[] float32;

    public float[] getFloat32() {
        return float32;
    }

    public VectorData setFloat32(float[] float32) {
        this.float32 = float32;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private float[] float32;

        public Builder float32(float[] float32) {
            this.float32 = float32;
            return this;
        }

        public VectorData build() {
            VectorData vectorData = new VectorData();
            vectorData.setFloat32(float32);
            return vectorData;
        }
    }

    @Override
    public String toString() {
        return "VectorData{" +
                "float32=" + (float32 != null ? "[" + float32.length + " elements]" : "null") +
                '}';
    }
}