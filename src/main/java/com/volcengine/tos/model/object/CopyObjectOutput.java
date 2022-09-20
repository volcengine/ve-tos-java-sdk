package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Deprecated
public class CopyObjectOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("VersionId")
    private String versionID;
    @JsonProperty("SourceVersionId")
    private String sourceVersionID;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("LastModified")
    private Date lastModified;
    private String crc64;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CopyObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public CopyObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getSourceVersionID() {
        return sourceVersionID;
    }

    public CopyObjectOutput setSourceVersionID(String sourceVersionID) {
        this.sourceVersionID = sourceVersionID;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public CopyObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public CopyObjectOutput setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getCrc64() {
        return crc64;
    }

    public CopyObjectOutput setCrc64(String crc64) {
        this.crc64 = crc64;
        return this;
    }

    @Override
    public String toString() {
        return "CopyObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", sourceVersionID='" + sourceVersionID + '\'' +
                ", etag='" + etag + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", crc64=" + crc64 +
                '}';
    }
}
