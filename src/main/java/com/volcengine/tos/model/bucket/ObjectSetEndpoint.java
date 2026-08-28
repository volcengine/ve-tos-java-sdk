package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ObjectSetEndpoint {
    @JsonProperty("capname")
    private String capname;

    @JsonProperty("endpoint")
    private List<String> endpoint;

    @JsonProperty("s3endpoint")
    private List<String> s3endpoint;

    public String getCapname() {
        return capname;
    }

    public ObjectSetEndpoint setCapname(String capname) {
        this.capname = capname;
        return this;
    }

    public List<String> getEndpoint() {
        return endpoint;
    }

    public ObjectSetEndpoint setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public List<String> getS3endpoint() {
        return s3endpoint;
    }

    public ObjectSetEndpoint setS3endpoint(List<String> s3endpoint) {
        this.s3endpoint = s3endpoint;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetEndpoint{" +
                "capname='" + capname + '\'' +
                ", endpoint=" + endpoint +
                ", s3endpoint=" + s3endpoint +
                '}';
    }
}
