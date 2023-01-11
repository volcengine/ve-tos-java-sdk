package com.volcengine.tos.model.object;

import java.io.Serializable;

public class UploadFilePartInfo implements Serializable {
    private UploadPartOutput part;
    private long offset;
    private int partNum;
    private long partSize;
    private boolean isCompleted;

    public UploadPartOutput getPart() {
        return part;
    }

    public UploadFilePartInfo setPart(UploadPartOutput part) {
        this.part = part;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public UploadFilePartInfo setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public UploadFilePartInfo setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public int getPartNum() {
        return partNum;
    }

    public UploadFilePartInfo setPartNum(int partNum) {
        this.partNum = partNum;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadFilePartInfo setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFilePartInfo{" +
                "part=" + part +
                ", offset=" + offset +
                ", partNum=" + partNum +
                ", partSize=" + partSize +
                ", isCompleted=" + isCompleted +
                '}';
    }
}