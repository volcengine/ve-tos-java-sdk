package com.volcengine.tos.auth;

public class SignKeyInfo {
    private String date;
    private String region;
    private String sk;

    public SignKeyInfo() {
    }

    SignKeyInfo(String date, String region, String sk) {
        this.date = date;
        this.region = region;
        this.sk = sk;
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

    public String getSk() {
        return sk;
    }

    public SignKeyInfo setSk(String sk) {
        this.sk = sk;
        return this;
    }

    @Override
    public String toString() {
        return "SignKeyInfo{" +
                "date='" + date + '\'' +
                ", region='" + region + '\'' +
                ", sk=" + sk +
                '}';
    }
}
