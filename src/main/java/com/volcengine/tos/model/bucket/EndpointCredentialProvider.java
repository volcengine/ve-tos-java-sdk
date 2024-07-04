package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EndpointCredentialProvider {
    @JsonProperty("Endpoint")
    private String endpoint;
    @JsonProperty("BucketName")
    private String bucketName;
    @JsonProperty("CredentialProvider")
    private CredentialProvider credentialProvider;

    public String getEndpoint() {
        return endpoint;
    }

    public EndpointCredentialProvider setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public EndpointCredentialProvider setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    public EndpointCredentialProvider setCredentialProvider(CredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointCredentialProvider{" +
                "endpoint='" + endpoint + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", credentialProvider=" + credentialProvider +
                '}';
    }
}
