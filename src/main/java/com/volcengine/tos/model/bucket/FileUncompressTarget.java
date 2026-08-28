package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUncompressTarget {
    @JsonProperty("Region")
    private String region;

    @JsonProperty("Bucket")
    private String bucket;

    public String getRegion() {
        return region;
    }

    public FileUncompressTarget setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public FileUncompressTarget setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "FileUncompressTarget{" +
                "region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}
