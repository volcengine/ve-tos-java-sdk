package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicSource {
    @JsonProperty("SourceEndpoint")
    private SourceEndpoint sourceEndpoint;

    public SourceEndpoint getSourceEndpoint() {
        return sourceEndpoint;
    }

    public PublicSource setSourceEndpoint(SourceEndpoint sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "PublicSource{" +
                "sourceEndpoint=" + sourceEndpoint +
                '}';
    }

    public static PublicSourceBuilder builder() {
        return new PublicSourceBuilder();
    }

    public static final class PublicSourceBuilder {
        private SourceEndpoint sourceEndpoint;

        private PublicSourceBuilder() {
        }

        public PublicSourceBuilder sourceEndpoint(SourceEndpoint sourceEndpoint) {
            this.sourceEndpoint = sourceEndpoint;
            return this;
        }

        public PublicSource build() {
            PublicSource publicSource = new PublicSource();
            publicSource.setSourceEndpoint(sourceEndpoint);
            return publicSource;
        }
    }
}
