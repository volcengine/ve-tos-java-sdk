package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SourceEndpoint {
    @JsonProperty("Primary")
    private List<String> primary;
    @JsonProperty("Follower")
    private List<String> follower;

    public List<String> getPrimary() {
        return primary;
    }

    public SourceEndpoint setPrimary(List<String> primary) {
        this.primary = primary;
        return this;
    }

    public List<String> getFollower() {
        return follower;
    }

    public SourceEndpoint setFollower(List<String> follower) {
        this.follower = follower;
        return this;
    }

    @Override
    public String toString() {
        return "SourceEndpoint{" +
                "primary=" + primary +
                ", follower=" + follower +
                '}';
    }

    public static SourceEndpointBuilder builder() {
        return new SourceEndpointBuilder();
    }

    public static final class SourceEndpointBuilder {
        private List<String> primary;
        private List<String> follower;

        private SourceEndpointBuilder() {
        }

        public SourceEndpointBuilder primary(List<String> primary) {
            this.primary = primary;
            return this;
        }

        public SourceEndpointBuilder follower(List<String> follower) {
            this.follower = follower;
            return this;
        }

        public SourceEndpoint build() {
            SourceEndpoint sourceEndpoint = new SourceEndpoint();
            sourceEndpoint.setPrimary(primary);
            sourceEndpoint.setFollower(follower);
            return sourceEndpoint;
        }
    }
}
