package com.volcengine.tos.auth;

class SignKeyInfo {
    private String date;
    private String region;
    private Credential credential;

    SignKeyInfo() {
    }

    SignKeyInfo(String date, String region, Credential credential){
        this.date = date;
        this.region = region;
        this.credential = credential;
    }

    public String getDate() {
        return date;
    }

    public SignKeyInfo setDate(String date) {
        this.date = date;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public SignKeyInfo setRegion(String region) {
        this.region = region;
        return this;
    }

    public Credential getCredential() {
        return credential;
    }

    public SignKeyInfo setCredential(Credential credential) {
        this.credential = credential;
        return this;
    }

    @Override
    public String toString() {
        return "SignKeyInfo{" +
                "date='" + date + '\'' +
                ", region='" + region + '\'' +
                ", credential=" + credential +
                '}';
    }
}
