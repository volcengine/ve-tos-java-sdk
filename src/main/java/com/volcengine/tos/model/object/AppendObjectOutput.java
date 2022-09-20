package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import org.apache.commons.lang3.StringUtils;

public class AppendObjectOutput {
    private RequestInfo requestInfo;
    private String versionID;
    private String hashCrc64ecma;
    private long nextAppendOffset;
    @Deprecated
    private String etag;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public AppendObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Deprecated
    public String getEtag() {
        return etag;
    }

    @Deprecated
    public AppendObjectOutput setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getNextAppendOffset() {
        return nextAppendOffset;
    }

    public AppendObjectOutput setNextAppendOffset(long nextAppendOffset) {
        this.nextAppendOffset = nextAppendOffset;
        return this;
    }

    @Deprecated
    public String getCrc64() {
        return hashCrc64ecma;
    }

    @Deprecated
    public AppendObjectOutput setCrc64(String crc64) {
        this.hashCrc64ecma = crc64;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public AppendObjectOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public AppendObjectOutput setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    @Override
    public String toString() {
        return "AppendObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", hashCrc64ecma=" + hashCrc64ecma +
                ", nextAppendOffset=" + nextAppendOffset +
                '}';
    }
}
