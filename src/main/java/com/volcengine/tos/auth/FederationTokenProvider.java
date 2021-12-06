package com.volcengine.tos.auth;

public interface FederationTokenProvider {
    /**
     * provide federation token
     * @return FederationToken
     */
    FederationToken federationToken();
}
