package com.volcengine.tos.model.object;

import java.util.Map;

public class PreSignedURLOutput {
    private String signedUrl;
    private Map<String,String> signedHeader;

    public PreSignedURLOutput(String signedUrl, Map<String, String> signedHeader) {
        this.signedUrl = signedUrl;
        this.signedHeader = signedHeader;
    }

    public String getSignedUrl() {
        return signedUrl;
    }

    public Map<String, String> getSignedHeader() {
        return signedHeader;
    }

    public PreSignedURLOutput setSignedUrl(String signedUrl) {
        this.signedUrl = signedUrl;
        return this;
    }

    public PreSignedURLOutput setSignedHeader(Map<String, String> signedHeader) {
        this.signedHeader = signedHeader;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedURLOutput{" +
                "signedUrl='" + signedUrl + '\'' +
                ", signedHeader=" + signedHeader +
                '}';
    }
}
