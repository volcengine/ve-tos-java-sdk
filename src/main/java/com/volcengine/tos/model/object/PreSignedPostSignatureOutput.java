package com.volcengine.tos.model.object;

public class PreSignedPostSignatureOutput {
    private String originPolicy;
    private String policy;
    private String algorithm;
    private String credential;
    private String date;
    private String signature;

    public String getOriginPolicy() {
        return originPolicy;
    }

    public PreSignedPostSignatureOutput setOriginPolicy(String originPolicy) {
        this.originPolicy = originPolicy;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public PreSignedPostSignatureOutput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public PreSignedPostSignatureOutput setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public String getCredential() {
        return credential;
    }

    public PreSignedPostSignatureOutput setCredential(String credential) {
        this.credential = credential;
        return this;
    }

    public String getDate() {
        return date;
    }

    public PreSignedPostSignatureOutput setDate(String date) {
        this.date = date;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public PreSignedPostSignatureOutput setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedPostSignatureOutput{" +
                "originPolicy='" + originPolicy + '\'' +
                ", policy='" + policy + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", credential='" + credential + '\'' +
                ", date='" + date + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
