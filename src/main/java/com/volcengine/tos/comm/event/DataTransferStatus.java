package com.volcengine.tos.comm.event;

public class DataTransferStatus {
    private long consumedBytes;
    private long totalBytes;
    private long rwOnceBytes;
    private DataTransferType type;

    public long getConsumedBytes() {
        return consumedBytes;
    }

    public DataTransferStatus setConsumedBytes(long consumedBytes) {
        this.consumedBytes = consumedBytes;
        return this;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public DataTransferStatus setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
        return this;
    }

    public long getRwOnceBytes() {
        return rwOnceBytes;
    }

    public DataTransferStatus setRwOnceBytes(long rwOnceBytes) {
        this.rwOnceBytes = rwOnceBytes;
        return this;
    }

    public DataTransferType getType() {
        return type;
    }

    public DataTransferStatus setType(DataTransferType type) {
        this.type = type;
        return this;
    }
}
