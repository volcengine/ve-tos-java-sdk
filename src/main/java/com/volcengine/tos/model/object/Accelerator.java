package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Accelerator {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("AZ")
    private String az;

    public Accelerator() {
    }

    public String getId() {
        return id;
    }

    public Accelerator setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Accelerator setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Accelerator setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getAz() {
        return az;
    }

    public Accelerator setAz(String az) {
        this.az = az;
        return this;
    }

    @Override
    public String toString() {
        return "Accelerator{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", az='" + az + '\'' +
                '}';
    }
}
