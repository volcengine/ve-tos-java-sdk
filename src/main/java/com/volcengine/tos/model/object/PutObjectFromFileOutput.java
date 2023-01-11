package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class PutObjectFromFileOutput {
    private PutObjectOutput putObjectOutput = new PutObjectOutput();

    public PutObjectFromFileOutput() {
    }

    public RequestInfo getRequestInfo() {
        return putObjectOutput.getRequestInfo();
    }

    public PutObjectFromFileOutput setRequestInfo(RequestInfo requestInfo) {
        this.putObjectOutput.setRequestInfo(requestInfo);
        return this;
    }

    public String getEtag() {
        return putObjectOutput.getEtag();
    }

    public PutObjectFromFileOutput setEtag(String etag) {
        this.putObjectOutput.setEtag(etag);
        return this;
    }

    public String getVersionID() {
        return putObjectOutput.getVersionID();
    }

    public PutObjectFromFileOutput setVersionID(String versionID) {
        this.putObjectOutput.setVersionID(versionID);
        return this;
    }

    public String getHashCrc64ecma() {
        return putObjectOutput.getHashCrc64ecma();
    }

    public PutObjectFromFileOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.putObjectOutput.setHashCrc64ecma(hashCrc64ecma);
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return putObjectOutput.getSseCustomerAlgorithm();
    }

    public PutObjectFromFileOutput setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.putObjectOutput.setSseCustomerAlgorithm(sseCustomerAlgorithm);
        return this;
    }

    public String getSseCustomerKeyMD5() {
        return putObjectOutput.getSseCustomerKeyMD5();
    }

    public PutObjectFromFileOutput setSseCustomerKeyMD5(String sseCustomerKeyMD5) {
        this.putObjectOutput.setSseCustomerKeyMD5(sseCustomerKeyMD5);
        return this;
    }

    public String getSseCustomerKey() {
        return putObjectOutput.getSseCustomerKey();
    }

    public PutObjectFromFileOutput setSseCustomerKey(String sseCustomerKey) {
        this.putObjectOutput.setSseCustomerKey(sseCustomerKey);
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectFromFileOutput{" +
                "requestInfo=" + getRequestInfo() +
                ", etag='" + getEtag() + '\'' +
                ", versionID='" + getVersionID() + '\'' +
                ", hashCrc64ecma=" + getHashCrc64ecma() +
                ", sseCustomerAlgorithm='" + getSseCustomerAlgorithm() + '\'' +
                ", sseCustomerKeyMD5='" + getSseCustomerKeyMD5() + '\'' +
                ", sseCustomerKey='" + getSseCustomerKey() + '\'' +
                '}';
    }

    @Deprecated
    public PutObjectFromFileOutput(PutObjectOutput putObjectOutput) {
        this.putObjectOutput = putObjectOutput;
    }

    @Deprecated
    public PutObjectOutput getPutObjectOutput() {
        return putObjectOutput;
    }

    @Deprecated
    public PutObjectFromFileOutput setPutObjectOutput(PutObjectOutput putObjectOutput) {
        this.putObjectOutput = putObjectOutput;
        return this;
    }
}
