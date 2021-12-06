package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

public class CreateMultipartUploadOutput {
    private RequestInfo requestInfo;
    private String bucket;
    private String key;
    private String uploadID;
    private String sseCustomerAlgorithm;
    private String sseCustomerMD5;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateMultipartUploadOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public CreateMultipartUploadOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CreateMultipartUploadOutput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public CreateMultipartUploadOutput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public CreateMultipartUploadOutput setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
        return this;
    }

    public String getSseCustomerMD5() {
        return sseCustomerMD5;
    }

    public CreateMultipartUploadOutput setSseCustomerMD5(String sseCustomerMD5) {
        this.sseCustomerMD5 = sseCustomerMD5;
        return this;
    }

    @Override
    public String toString() {
        return "CreateMultipartUploadOutput{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", sseCustomerAlgorithm='" + sseCustomerAlgorithm + '\'' +
                ", sseCustomerMD5='" + sseCustomerMD5 + '\'' +
                '}';
    }
}
