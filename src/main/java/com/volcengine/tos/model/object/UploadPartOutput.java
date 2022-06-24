package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;

public class UploadPartOutput implements MultipartUploadedPart, Serializable {
    @JsonIgnore
    private RequestInfo requestInfo;
    private int partNumber;
    private String etag;
    private String sseCustomerAlgorithm;
    private String sseCustomerMD5;

    @Override
    public InnerUploadedPart uploadedPart() {
        return new InnerUploadedPart(this.partNumber, this.etag);
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public UploadPartOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartOutput setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadPartOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public UploadPartOutput setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
        return this;
    }

    public String getSseCustomerMD5() {
        return sseCustomerMD5;
    }

    public UploadPartOutput setSseCustomerMD5(String sseCustomerMD5) {
        this.sseCustomerMD5 = sseCustomerMD5;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartOutput{" +
                "requestInfo=" + requestInfo +
                ", partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", sseCustomerAlgorithm='" + sseCustomerAlgorithm + '\'' +
                ", sseCustomerMD5='" + sseCustomerMD5 + '\'' +
                '}';
    }
}
