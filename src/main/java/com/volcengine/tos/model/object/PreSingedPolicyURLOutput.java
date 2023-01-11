package com.volcengine.tos.model.object;

public class PreSingedPolicyURLOutput {
    private PreSignedPolicyURLGenerator preSignedPolicyURLGenerator;
    private String signatureQuery;
    private String host;
    private String scheme;

    public PreSignedPolicyURLGenerator getPreSignedPolicyURLGenerator() {
        return preSignedPolicyURLGenerator;
    }

    public PreSingedPolicyURLOutput setPreSignedPolicyURLGenerator(PreSignedPolicyURLGenerator preSignedPolicyURLGenerator) {
        this.preSignedPolicyURLGenerator = preSignedPolicyURLGenerator;
        return this;
    }

    public String getSignatureQuery() {
        return signatureQuery;
    }

    public PreSingedPolicyURLOutput setSignatureQuery(String signatureQuery) {
        this.signatureQuery = signatureQuery;
        return this;
    }

    public String getHost() {
        return host;
    }

    public PreSingedPolicyURLOutput setHost(String host) {
        this.host = host;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public PreSingedPolicyURLOutput setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public String toString() {
        return "PreSingedPolicyURLOutput{" +
                "preSignedPolicyURLGenerator=" + preSignedPolicyURLGenerator +
                ", signatureQuery='" + signatureQuery + '\'' +
                ", host='" + host + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}
