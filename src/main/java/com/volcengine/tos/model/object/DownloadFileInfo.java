package com.volcengine.tos.model.object;

public class DownloadFileInfo {
    private String filePath;
    private String tempFilePath;

    public String getFilePath() {
        return filePath;
    }

    public DownloadFileInfo setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public DownloadFileInfo setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadFileInfo{" +
                "filePath='" + filePath + '\'' +
                ", tempFilePath='" + tempFilePath + '\'' +
                '}';
    }
}
