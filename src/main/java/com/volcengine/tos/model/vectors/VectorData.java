package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorData {
    @JsonProperty("float32")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float[] float32;

    // 服务端期望 int8 是 JSON 数字数组 [-128, 127]，
    // 而 Jackson 默认会把 byte[] 序列化为 base64 字符串，所以这里强制按数组处理。
    @JsonProperty("int8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = Int8ArraySerializer.class)
    @JsonDeserialize(using = Int8ArrayDeserializer.class)
    private byte[] int8;

    public float[] getFloat32() {
        return float32;
    }

    public VectorData setFloat32(float[] float32) {
        this.float32 = float32;
        return this;
    }

    public byte[] getInt8() {
        return int8;
    }

    public VectorData setInt8(byte[] int8) {
        this.int8 = int8;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private float[] float32;
        private byte[] int8;

        public Builder float32(float[] float32) {
            this.float32 = float32;
            return this;
        }

        public Builder int8(byte[] int8) {
            this.int8 = int8;
            return this;
        }

        public VectorData build() {
            VectorData vectorData = new VectorData();
            vectorData.setFloat32(float32);
            vectorData.setInt8(int8);
            return vectorData;
        }
    }

    @Override
    public String toString() {
        return "VectorData{" +
                "float32=" + (float32 != null ? "[" + float32.length + " elements]" : "null") +
                ", int8=" + (int8 != null ? "[" + int8.length + " elements]" : "null") +
                '}';
    }

    public static class Int8ArraySerializer extends JsonSerializer<byte[]> {
        @Override
        public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray(value.length);
            for (byte b : value) {
                gen.writeNumber((int) b);
            }
            gen.writeEndArray();
        }
    }

    public static class Int8ArrayDeserializer extends JsonDeserializer<byte[]> {
        @Override
        public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() != JsonToken.START_ARRAY) {
                return (byte[]) ctxt.handleUnexpectedToken(byte[].class, p);
            }
            List<Byte> tmp = new ArrayList<>();
            while (p.nextToken() != JsonToken.END_ARRAY) {
                tmp.add((byte) p.getIntValue());
            }
            byte[] arr = new byte[tmp.size()];
            for (int i = 0; i < tmp.size(); i++) {
                arr[i] = tmp.get(i);
            }
            return arr;
        }
    }
}
