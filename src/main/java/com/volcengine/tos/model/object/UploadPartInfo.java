package com.volcengine.tos.model.object;

public class UploadPartInfo {
    private int partNumber;
    private long partSize;
    private long offset;

    private String etag;
    private long hashCrc64ecma;

    private boolean isCompleted;

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartInfo setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadPartInfo setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public UploadPartInfo setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadPartInfo setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public UploadPartInfo setHashCrc64ecma(long hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public UploadPartInfo setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartInfo{" +
                "partNumber=" + partNumber +
                ", partSize=" + partSize +
                ", offset=" + offset +
                ", etag='" + etag + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
