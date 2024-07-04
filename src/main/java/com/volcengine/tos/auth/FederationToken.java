package com.volcengine.tos.auth;

import java.time.LocalDateTime;

@Deprecated
public class FederationToken {
    private Credential credential;
    private LocalDateTime expiration;

    public Credential getCredential() {
        return credential;
    }

    public FederationToken setCredential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public FederationToken setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    @Override
    public String toString() {
        return "FederationToken{" +
                "credential=" + credential +
                ", expiration=" + expiration +
                '}';
    }
}
