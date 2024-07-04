package com.volcengine.tos.auth;

@Deprecated
public interface FederationTokenProvider {
    /**
     * provide federation token
     * @return FederationToken
     */
    FederationToken federationToken();
}
