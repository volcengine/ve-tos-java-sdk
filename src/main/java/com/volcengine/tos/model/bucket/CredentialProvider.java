package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialProvider {
    @JsonProperty("Role")
    private String role;

    public String getRole() {
        return role;
    }

    public CredentialProvider setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "CredentialProvider{" +
                "role='" + role + '\'' +
                '}';
    }
}
