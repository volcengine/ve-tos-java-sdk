package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivateSource {
    @JsonProperty("SourceEndpoint")
    private CommonSourceEndpoint sourceEndpoint;

    public CommonSourceEndpoint getSourceEndpoint() {
        return sourceEndpoint;
    }

    public PrivateSource setSourceEndpoint(CommonSourceEndpoint sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "PrivateSource{" +
                "sourceEndpoint=" + sourceEndpoint +
                '}';
    }
}
