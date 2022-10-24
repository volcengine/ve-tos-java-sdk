package com.volcengine.tos.model.object;

import java.util.Date;

public class DownloadObjectInfo {
    private String etag;
    private long objectSize;
    private String hashCrc64ecma;
    private Date lastModified;

    public String getEtag() {
        return etag;
    }

    public DownloadObjectInfo setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public DownloadObjectInfo setObjectSize(long objectSize) {
        this.objectSize = objectSize;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public DownloadObjectInfo setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public DownloadObjectInfo setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadObjectInfo{" +
                "etag='" + etag + '\'' +
                ", objectSize=" + objectSize +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}
