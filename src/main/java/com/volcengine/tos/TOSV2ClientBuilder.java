package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class TOSV2ClientBuilder implements TOSV2Builder {

    public TOSV2ClientBuilder() {
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey) {
        Credentials cred = new StaticCredentials(accessKey, secretKey);
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(cred).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken) {
        Credentials cred = new StaticCredentials(accessKey, secretKey).withSecurityToken(securityToken);
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(cred).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, Credentials credentials) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(credentials).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, TransportConfig conf) {
        Credentials cred = new StaticCredentials(accessKey, secretKey);
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(cred).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, Credentials credentials, TransportConfig conf) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(credentials).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken, TransportConfig conf) {
        Credentials cred = new StaticCredentials(accessKey, secretKey).withSecurityToken(securityToken);
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(cred).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(TOSClientConfiguration conf) {
        return new TOSV2Client(conf);
    }
}
