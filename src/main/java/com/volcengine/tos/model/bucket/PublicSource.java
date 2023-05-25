package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicSource {
    @JsonProperty("SourceEndpoint")
    private SourceEndpoint sourceEndpoint;
    @JsonProperty("FixedEndpoint")
    private boolean fixedEndpoint;

    public SourceEndpoint getSourceEndpoint() {
        return sourceEndpoint;
    }

    public PublicSource setSourceEndpoint(SourceEndpoint sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
        return this;
    }

    public boolean isFixedEndpoint() {
        return fixedEndpoint;
    }

    public PublicSource setFixedEndpoint(boolean fixedEndpoint) {
        this.fixedEndpoint = fixedEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "PublicSource{" +
                "sourceEndpoint=" + sourceEndpoint +
                ", fixedEndpoint=" + fixedEndpoint +
                '}';
    }

    public static PublicSourceBuilder builder() {
        return new PublicSourceBuilder();
    }

    public static final class PublicSourceBuilder {
        private SourceEndpoint sourceEndpoint;
        private boolean fixedEndpoint;

        private PublicSourceBuilder() {
        }

        public PublicSourceBuilder sourceEndpoint(SourceEndpoint sourceEndpoint) {
            this.sourceEndpoint = sourceEndpoint;
            return this;
        }

        public PublicSourceBuilder fixedEndpoint(boolean fixedEndpoint) {
            this.fixedEndpoint = fixedEndpoint;
            return this;
        }

        public PublicSource build() {
            PublicSource publicSource = new PublicSource();
            publicSource.setSourceEndpoint(sourceEndpoint);
            publicSource.setFixedEndpoint(fixedEndpoint);
            return publicSource;
        }
    }
}
