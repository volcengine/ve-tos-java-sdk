package com.volcengine.tos.model.object;

public class DownloadPartInfo {
    private int partNumber;
    private long rangeStart;
    private long rangeEnd;
    private boolean isCompleted;
    private long hashCrc64ecma;

    public int getPartNumber() {
        return partNumber;
    }

    public DownloadPartInfo setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public long getRangeStart() {
        return rangeStart;
    }

    public DownloadPartInfo setRangeStart(long rangeStart) {
        this.rangeStart = rangeStart;
        return this;
    }

    public long getRangeEnd() {
        return rangeEnd;
    }

    public DownloadPartInfo setRangeEnd(long rangeEnd) {
        this.rangeEnd = rangeEnd;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public DownloadPartInfo setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public long getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public DownloadPartInfo setHashCrc64ecma(long hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadPartInfo{" +
                "partNumber=" + partNumber +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", isCompleted=" + isCompleted +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                '}';
    }
}
