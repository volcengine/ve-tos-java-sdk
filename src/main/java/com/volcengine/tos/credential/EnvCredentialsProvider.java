package com.volcengine.tos.credential;

public class EnvCredentialsProvider implements CredentialsProvider {
    private final Credentials credentials;

    public EnvCredentialsProvider() {
        CommonCredentials credentials = new CommonCredentials(System.getenv("TOS_ACCESS_KEY"),
                System.getenv("TOS_SECRET_KEY"), System.getenv("TOS_SECURITY_TOKEN"));
        this.credentials = credentials;
    }

    @Override
    public Credentials getCredentials(int expires) {
        return credentials;
    }
}
