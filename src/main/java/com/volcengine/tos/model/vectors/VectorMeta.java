package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * VectorMeta 用于读取响应（GetVectors / ListVectors）。
 * 服务端不再返回 vector data，因此该类型只暴露 key / metadata。
 */
public class VectorMeta {
    @JsonProperty("key")
    private String key;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    public String getKey() {
        return key;
    }

    public VectorMeta setKey(String key) {
        this.key = key;
        return this;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public VectorMeta setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public String toString() {
        return "VectorMeta{" +
                "key='" + key + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
