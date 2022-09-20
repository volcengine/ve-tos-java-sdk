package com.volcengine.tos;

import com.volcengine.tos.transport.TransportConfig;

@Deprecated
public class Config {
    private String endpoint;
    private String region;
    private TransportConfig transportConfig;

    public Config defaultConfig(){
        this.transportConfig = new TransportConfig().defaultTransportConfig();
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Config setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Config setRegion(String region) {
        this.region = region;
        return this;
    }

    public TransportConfig getTransportConfig() {
        return transportConfig;
    }

    public Config setTransportConfig(TransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }
}
