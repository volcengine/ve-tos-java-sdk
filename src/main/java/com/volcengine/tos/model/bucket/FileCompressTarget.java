package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileCompressTarget {
    @JsonProperty("Region")
    private String region;

    @JsonProperty("Bucket")
    private String bucket;

    @JsonProperty("Object")
    private String object;

    public String getRegion() {
        return region;
    }

    public FileCompressTarget setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public FileCompressTarget setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObject() {
        return object;
    }

    public FileCompressTarget setObject(String object) {
        this.object = object;
        return this;
    }

    @Override
    public String toString() {
        return "FileCompressTarget{" +
                "region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                ", object='" + object + '\'' +
                '}';
    }
}
