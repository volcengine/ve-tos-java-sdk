package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class FetchObjectOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonIgnore
    private String versionID;
    @JsonProperty("ETag")
    private String etag;
    @JsonIgnore
    private String ssecAlgorithm;
    @JsonIgnore
    private String ssecKeyMD5;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public FetchObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public FetchObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public FetchObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public FetchObjectOutput setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public FetchObjectOutput setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    @Override
    public String toString() {
        return "FetchObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", etag='" + etag + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                '}';
    }
}
