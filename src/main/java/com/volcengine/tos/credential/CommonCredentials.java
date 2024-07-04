package com.volcengine.tos.credential;

class CommonCredentials implements Credentials{
    private final String ak;
    private final String sk;
    private final String securityToken;

    CommonCredentials(String ak, String sk, String securityToken) {
        this.ak = ak;
        this.sk = sk;
        this.securityToken = securityToken;
    }

    @Override
    public String getAk() {
        return ak;
    }

    @Override
    public String getSk() {
        return sk;
    }

    @Override
    public String getSecurityToken() {
        return securityToken;
    }
}
