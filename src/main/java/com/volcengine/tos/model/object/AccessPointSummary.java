package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessPointSummary {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Alias")
    private String alias;

    @JsonProperty("Endpoints")
    private AccessPointEndpoints endpoints;

    public AccessPointSummary() {
    }

    public String getName() {
        return name;
    }

    public AccessPointSummary setName(String name) {
        this.name = name;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public AccessPointSummary setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public AccessPointEndpoints getEndpoints() {
        return endpoints;
    }

    public AccessPointSummary setEndpoints(AccessPointEndpoints endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    @Override
    public String toString() {
        return "AccessPointSummary{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", endpoints=" + endpoints +
                '}';
    }
}
