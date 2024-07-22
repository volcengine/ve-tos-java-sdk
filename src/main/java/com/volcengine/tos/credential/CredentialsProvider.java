package com.volcengine.tos.credential;

public interface CredentialsProvider {

    Credentials getCredentials(int expires);
}
