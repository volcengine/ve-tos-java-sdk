package com.volcengine.tos.model.object;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.UploadEventType;

public class UploadEvent {
    private UploadEventType uploadEventType;
    private TosException tosException;

    private String bucket;
    private String key;
    private String uploadID;
    private String filePath;
    private String checkpointFile;

    private UploadPartInfo uploadPartInfo;

    public UploadEventType getUploadEventType() {
        return uploadEventType;
    }

    public UploadEvent setUploadEventType(UploadEventType uploadEventType) {
        this.uploadEventType = uploadEventType;
        return this;
    }

    public TosException getTosException() {
        return tosException;
    }

    public UploadEvent setTosException(TosException tosException) {
        this.tosException = tosException;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public UploadEvent setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public UploadEvent setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadEvent setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public UploadEvent setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public UploadEvent setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public UploadPartInfo getUploadPartInfo() {
        return uploadPartInfo;
    }

    public UploadEvent setUploadPartInfo(UploadPartInfo uploadPartInfo) {
        this.uploadPartInfo = uploadPartInfo;
        return this;
    }

    @Override
    public String toString() {
        return "UploadEvent{" +
                "uploadEventType=" + uploadEventType +
                ", tosException=" + tosException +
                ", buckets='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", filePath='" + filePath + '\'' +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", uploadPartInfo=" + uploadPartInfo +
                '}';
    }
}
