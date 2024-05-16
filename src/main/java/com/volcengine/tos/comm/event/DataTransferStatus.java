package com.volcengine.tos.comm.event;

public class DataTransferStatus {
    private long consumedBytes;
    private long totalBytes;
    /**
     * rwOnceBytes has value when DataTransferType is DATA_TRANSFER_RW
     */
    private long rwOnceBytes;
    private DataTransferType type;

    private int retryCount = -1;

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

    public int getRetryCount() {
        return retryCount;
    }

    public DataTransferStatus setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    @Override
    public String toString() {
        return "DataTransferStatus{" +
                "consumedBytes=" + consumedBytes +
                ", totalBytes=" + totalBytes +
                ", rwOnceBytes=" + rwOnceBytes +
                ", type=" + type +
                ", retryCount=" + retryCount +
                '}';
    }
}
