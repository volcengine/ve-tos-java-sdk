package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadedPart {
    @JsonProperty("PartNumber")
    private int partNumber;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("Size")
    private long size;

    public int getPartNumber() {
        return partNumber;
    }

    public UploadedPart setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public UploadedPart setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadedPart setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getSize() {
        return size;
    }

    public UploadedPart setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "UploadedPart{" +
                "partNumber=" + partNumber +
                ", lastModified='" + lastModified + '\'' +
                ", etag='" + etag + '\'' +
                ", size=" + size +
                '}';
    }
}
