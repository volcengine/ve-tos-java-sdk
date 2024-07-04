package com.volcengine.tos.credential;

public class StaticCredentialsProvider implements CredentialsProvider {
    private final Credentials credentials;

    public StaticCredentialsProvider(String ak, String sk, String securityToken) {
        CommonCredentials credentials = new CommonCredentials(ak, sk, securityToken);
        this.credentials = credentials;
    }

    public StaticCredentialsProvider(String ak, String sk) {
        this(ak, sk, null);
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
}
