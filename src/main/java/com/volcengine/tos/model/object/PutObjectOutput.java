package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PutObjectOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("VersionId")
    private String versionID;
    @JsonProperty("SSECustomerAlgorithm")
    private String sseCustomerAlgorithm;
    @JsonProperty("SSECustomerKeyMD5")
    private String sseCustomerKeyMD5;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public PutObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public PutObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public PutObjectOutput setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
        return this;
    }

    public String getSseCustomerKeyMD5() {
        return sseCustomerKeyMD5;
    }

    public PutObjectOutput setSseCustomerKeyMD5(String sseCustomerKeyMD5) {
        this.sseCustomerKeyMD5 = sseCustomerKeyMD5;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", etag='" + etag + '\'' +
                ", versionID='" + versionID + '\'' +
                ", sseCustomerAlgorithm='" + sseCustomerAlgorithm + '\'' +
                ", sseCustomerKeyMD5='" + sseCustomerKeyMD5 + '\'' +
                '}';
    }
}
