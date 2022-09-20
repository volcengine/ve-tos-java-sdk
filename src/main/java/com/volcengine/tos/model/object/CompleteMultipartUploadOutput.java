package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;

@Deprecated
public class CompleteMultipartUploadOutput {
    private RequestInfo requestInfo;
    private String versionID;
    private String crc64;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CompleteMultipartUploadOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public CompleteMultipartUploadOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getCrc64() {
        return crc64;
    }

    public CompleteMultipartUploadOutput setCrc64(String crc64) {
        this.crc64 = crc64;
        return this;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", crc64=" + crc64 +
                '}';
    }
}
