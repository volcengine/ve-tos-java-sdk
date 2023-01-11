package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class UploadPartFromFileOutput {
    private UploadPartV2Output uploadPartV2Output = new UploadPartV2Output();

    public UploadPartFromFileOutput() {
    }

    public RequestInfo getRequestInfo() {
        return uploadPartV2Output.getRequestInfo();
    }

    public UploadPartFromFileOutput setRequestInfo(RequestInfo requestInfo) {
        this.uploadPartV2Output.setRequestInfo(requestInfo);
        return this;
    }

    public int getPartNumber() {
        return uploadPartV2Output.getPartNumber();
    }

    public UploadPartFromFileOutput setPartNumber(int partNumber) {
        this.uploadPartV2Output.setPartNumber(partNumber);
        return this;
    }

    public String getEtag() {
        return uploadPartV2Output.getEtag();
    }

    public UploadPartFromFileOutput setEtag(String etag) {
        this.uploadPartV2Output.setEtag(etag);
        return this;
    }

    public String getSsecAlgorithm() {
        return uploadPartV2Output.getSsecAlgorithm();
    }

    public UploadPartFromFileOutput setSsecAlgorithm(String ssecAlgorithm) {
        this.uploadPartV2Output.setSsecAlgorithm(ssecAlgorithm);
        return this;
    }

    public String getSsecKeyMD5() {
        return uploadPartV2Output.getSsecKeyMD5();
    }

    public UploadPartFromFileOutput setSsecKeyMD5(String ssecKeyMD5) {
        this.uploadPartV2Output.setSsecKeyMD5(ssecKeyMD5);
        return this;
    }

    public String getHashCrc64ecma() {
        return uploadPartV2Output.getHashCrc64ecma();
    }

    public UploadPartFromFileOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.uploadPartV2Output.setHashCrc64ecma(hashCrc64ecma);
        return this;
    }

    @Deprecated
    public UploadPartFromFileOutput(UploadPartV2Output uploadPartV2Output) {
        this.uploadPartV2Output = uploadPartV2Output;
    }

    @Deprecated
    public UploadPartV2Output getUploadPartV2Output() {
        return uploadPartV2Output;
    }

    @Override
    public String toString() {
        return "UploadPartFromFileOutput{" +
                "requestInfo=" + getRequestInfo() +
                ", partNumber=" + getPartNumber() +
                ", etag='" + getEtag() + '\'' +
                ", ssecAlgorithm='" + getSsecAlgorithm() + '\'' +
                ", ssecKeyMD5='" + getSsecKeyMD5() + '\'' +
                ", hashCrc64ecma=" + getHashCrc64ecma() +
                '}';
    }
}
