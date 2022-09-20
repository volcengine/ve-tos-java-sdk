package com.volcengine.tos.model.object;

import java.io.Serializable;

public class UploadFileInfo implements Serializable {
    private String filePath;
    private long lastModified;
    private long fileSize;

    public String getFilePath() {
        return filePath;
    }

    public UploadFileInfo setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public long getLastModified() {
        return lastModified;
    }

    public UploadFileInfo setLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public UploadFileInfo setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFileInfo{" +
                "filePath='" + filePath + '\'' +
                ", lastModified=" + lastModified +
                ", fileSize=" + fileSize +
                '}';
    }
}