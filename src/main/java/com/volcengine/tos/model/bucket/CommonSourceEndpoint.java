package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CommonSourceEndpoint {
    @JsonProperty("Primary")
    private List<EndpointCredentialProvider> primary;
    @JsonProperty("Follower")
    private List<EndpointCredentialProvider> follower;

    public List<EndpointCredentialProvider> getPrimary() {
        return primary;
    }

    public CommonSourceEndpoint setPrimary(List<EndpointCredentialProvider> primary) {
        this.primary = primary;
        return this;
    }

    public List<EndpointCredentialProvider> getFollower() {
        return follower;
    }

    public CommonSourceEndpoint setFollower(List<EndpointCredentialProvider> follower) {
        this.follower = follower;
        return this;
    }

    @Override
    public String toString() {
        return "CommonSourceEndpoint{" +
                "primary=" + primary +
                ", follower=" + follower +
                '}';
    }
}
