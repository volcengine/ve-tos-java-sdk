package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.model.RequestInfo;

import java.util.Date;

public class GetFileStatusOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Size")
    private long size;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("CRC32")
    private String crc32;
    @JsonProperty("CRC64")
    private String crc64;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetFileStatusOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetFileStatusOutput setKey(String key) {
        this.key = key;
        return this;
    }

    public long getSize() {
        return size;
    }

    public GetFileStatusOutput setSize(long size) {
        this.size = size;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public Date getLastModifiedInDate() {
        return DateConverter.iso8601StringToDate(lastModified);
    }

    public GetFileStatusOutput setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getCrc32() {
        return crc32;
    }

    public GetFileStatusOutput setCrc32(String crc32) {
        this.crc32 = crc32;
        return this;
    }

    public String getCrc64() {
        return crc64;
    }

    public GetFileStatusOutput setCrc64(String crc64) {
        this.crc64 = crc64;
        return this;
    }

    @Override
    public String toString() {
        return "GetFileStatusOutput{" +
                "requestInfo=" + requestInfo +
                ", key='" + key + '\'' +
                ", size=" + size +
                ", lastModified='" + lastModified + '\'' +
                ", crc32='" + crc32 + '\'' +
                ", crc64='" + crc64 + '\'' +
                '}';
    }
}
