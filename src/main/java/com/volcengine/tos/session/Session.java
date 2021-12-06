package com.volcengine.tos.session;

import com.volcengine.tos.auth.Credentials;

import java.util.Arrays;

public class Session {
    /**
     * If init client through session, can only set your own httpclient
     * config through withTransportConfig method at first init.
     * The sessions will share the same config for the singleton httpclient.
      */
    private String endpoint;
    private String region;
    private Credentials credentials;
    private SessionOptionsBuilder[] options;

    public Session(String endpoint, String region, Credentials credentials, SessionOptionsBuilder...options){
        this.endpoint = endpoint;
        this.region = region;
        this.credentials = credentials;
        this.options = options;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Session setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Session setRegion(String region) {
        this.region = region;
        return this;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public Session setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public SessionOptionsBuilder[] getOptions() {
        return options;
    }

    public Session setOptions(SessionOptionsBuilder[] options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "Session{" +
                "endpoint='" + endpoint + '\'' +
                ", region='" + region + '\'' +
                ", credentials=" + credentials +
                ", options=" + Arrays.toString(options) +
                '}';
    }
}
