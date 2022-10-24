package com.volcengine.tos.model.object;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.DownloadEventType;

public class DownloadEvent {
    private DownloadEventType downloadEventType;
    private TosException tosException;

    private String bucket;
    private String key;
    private String versionID;
    private String filePath;
    private String checkpointFile;
    private String tempFilePath;

    private DownloadPartInfo downloadPartInfo;

    public DownloadEventType getDownloadEventType() {
        return downloadEventType;
    }

    public DownloadEvent setDownloadEventType(DownloadEventType downloadEventType) {
        this.downloadEventType = downloadEventType;
        return this;
    }

    public TosException getTosException() {
        return tosException;
    }

    public DownloadEvent setTosException(TosException tosException) {
        this.tosException = tosException;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public DownloadEvent setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DownloadEvent setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DownloadEvent setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public DownloadEvent setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public DownloadEvent setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public DownloadEvent setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        return this;
    }

    public DownloadPartInfo getDownloadPartInfo() {
        return downloadPartInfo;
    }

    public DownloadEvent setDownloadPartInfo(DownloadPartInfo downloadPartInfo) {
        this.downloadPartInfo = downloadPartInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadEvent{" +
                "type=" + downloadEventType +
                ", tosException=" + tosException +
                ", buckets='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", filePath='" + filePath + '\'' +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", tempFilePath='" + tempFilePath + '\'' +
                ", downloadPartInfo=" + downloadPartInfo +
                '}';
    }
}
