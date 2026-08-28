package com.volcengine.tos.model.vectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetadataConfiguration {
    @JsonProperty("nonFilterableMetadataKeys")
    private List<String> nonFilterableMetadataKeys;

    public List<String> getNonFilterableMetadataKeys() {
        return nonFilterableMetadataKeys;
    }

    public MetadataConfiguration setNonFilterableMetadataKeys(List<String> nonFilterableMetadataKeys) {
        this.nonFilterableMetadataKeys = nonFilterableMetadataKeys;
        return this;
    }

    @Override
    public String toString() {
        return "MetadataConfiguration{" +
                "nonFilterableMetadataKeys=" + nonFilterableMetadataKeys +
                '}';
    }
}