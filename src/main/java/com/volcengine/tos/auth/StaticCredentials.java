package com.volcengine.tos.auth;

public class StaticCredentials implements Credentials{
    private String accessKey;
    private String secretKey;
    private String securityToken;

    public StaticCredentials(String accessKeyId, String accessKeySecret) {
        this.accessKey = accessKeyId;
        this.secretKey = accessKeySecret;
    }

    public StaticCredentials withSecurityToken(String securityToken){
        this.securityToken = securityToken;
        return this;
    }

    @Override
    public Credential credential(){
        return new Credential(this.accessKey, this.secretKey, this.securityToken);
    }
}
