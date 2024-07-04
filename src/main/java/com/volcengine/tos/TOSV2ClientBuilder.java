package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.credential.CredentialsProvider;
import com.volcengine.tos.credential.StaticCredentialsProvider;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.transport.TransportConfig;

public class TOSV2ClientBuilder implements TOSV2Builder {

    public TOSV2ClientBuilder() {
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey);
        }
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey) && StringUtils.isNotEmpty(securityToken)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey, securityToken);
        }
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).build();
        return build(clientConfiguration);
    }

    @Override
    @Deprecated
    public TOSV2 build(String region, String endpoint, Credentials credentials) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(credentials).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, CredentialsProvider credentialsProvider) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(credentialsProvider).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, TransportConfig conf) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey);
        }
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    @Deprecated
    public TOSV2 build(String region, String endpoint, Credentials credentials, TransportConfig conf) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentials(credentials).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, CredentialsProvider credentialsProvider, TransportConfig conf) {
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(credentialsProvider).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken, TransportConfig conf) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey) && StringUtils.isNotEmpty(securityToken)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey, securityToken);
        }
        TOSClientConfiguration clientConfiguration = TOSClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).transportConfig(conf).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSV2 build(TOSClientConfiguration conf) {
        return new TOSV2Client(conf);
    }
}
