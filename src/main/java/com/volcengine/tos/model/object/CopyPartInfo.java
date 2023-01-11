package com.volcengine.tos.model.object;

public class CopyPartInfo {
    private int partNumber;
    private long copySourceRangeStart;
    private long copySourceRangeEnd;
    private String etag;
    private boolean isCompleted;

    public int getPartNumber() {
        return partNumber;
    }

    public CopyPartInfo setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public long getCopySourceRangeStart() {
        return copySourceRangeStart;
    }

    public CopyPartInfo setCopySourceRangeStart(long copySourceRangeStart) {
        this.copySourceRangeStart = copySourceRangeStart;
        return this;
    }

    public long getCopySourceRangeEnd() {
        return copySourceRangeEnd;
    }

    public CopyPartInfo setCopySourceRangeEnd(long copySourceRangeEnd) {
        this.copySourceRangeEnd = copySourceRangeEnd;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public CopyPartInfo setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public CopyPartInfo setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    @Override
    public String toString() {
        return "CopyPartInfo{" +
                "partNumber=" + partNumber +
                ", copySourceRangeStart=" + copySourceRangeStart +
                ", copySourceRangeEnd=" + copySourceRangeEnd +
                ", etag='" + etag + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
