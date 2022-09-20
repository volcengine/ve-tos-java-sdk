package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

import java.util.Date;

public class UploadPartCopyV2Output {
    private RequestInfo requestInfo;
    private int partNumber;
    private String etag;
    private Date lastModified;
    private String copySourceVersionID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public UploadPartCopyV2Output requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartCopyV2Output partNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadPartCopyV2Output etag(String etag) {
        this.etag = etag;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public UploadPartCopyV2Output lastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getCopySourceVersionID() {
        return copySourceVersionID;
    }

    public UploadPartCopyV2Output copySourceVersionID(String copySourceVersionID) {
        this.copySourceVersionID = copySourceVersionID;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartCopyV2Output{" +
                "requestInfo=" + requestInfo +
                ", partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", lastModified=" + lastModified +
                ", copySourceVersionID='" + copySourceVersionID + '\'' +
                '}';
    }
}
