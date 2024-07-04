package com.volcengine.tos.model.object;

import java.util.Date;
import java.util.Objects;

public class CopySourceObjectInfo {
    private String etag;
    private String hashCrc64ecma;
    private Date lastModified;
    private long objectSize;
    private boolean isSymlink;

    public String getEtag() {
        return etag;
    }

    public CopySourceObjectInfo setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public CopySourceObjectInfo setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public CopySourceObjectInfo setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public CopySourceObjectInfo setObjectSize(long objectSize) {
        this.objectSize = objectSize;
        return this;
    }

    public boolean isSymlink() {
        return isSymlink;
    }

    public CopySourceObjectInfo setSymlink(boolean symlink) {
        isSymlink = symlink;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CopySourceObjectInfo that = (CopySourceObjectInfo) o;
        return objectSize == that.objectSize && etag.equals(that.etag)
                && Objects.equals(hashCrc64ecma, that.hashCrc64ecma) && lastModified.equals(that.lastModified) && isSymlink == that.isSymlink;
    }

    @Override
    public int hashCode() {
        return Objects.hash(etag, hashCrc64ecma, lastModified, objectSize, isSymlink);
    }

    @Override
    public String toString() {
        return "CopySourceObjectInfo{" +
                "etag='" + etag + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", lastModified=" + lastModified +
                ", objectSize=" + objectSize +
                ", isSymlink=" + isSymlink +
                '}';
    }
}
