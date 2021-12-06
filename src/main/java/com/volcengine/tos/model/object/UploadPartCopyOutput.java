package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadPartCopyOutput implements MultipartUploadedPart{
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("VersionId")
    private String versionID;
    @JsonProperty("SourceVersionId")
    private String sourceVersionID;
    @JsonProperty("PartNumber")
    private int partNumber;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("LastModified")
    private String lastModified;

    @Override
    public InnerUploadedPart uploadedPart(){
        return new InnerUploadedPart(this.partNumber, this.etag);
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public UploadPartCopyOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public UploadPartCopyOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getSourceVersionID() {
        return sourceVersionID;
    }

    public UploadPartCopyOutput setSourceVersionID(String sourceVersionID) {
        this.sourceVersionID = sourceVersionID;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartCopyOutput setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadPartCopyOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public UploadPartCopyOutput setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartCopyOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", sourceVersionID='" + sourceVersionID + '\'' +
                ", partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
