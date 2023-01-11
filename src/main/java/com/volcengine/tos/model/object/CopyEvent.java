package com.volcengine.tos.model.object;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.CopyEventType;

public class CopyEvent {
    private CopyEventType type;
    private TosException exception;

    private String bucket;
    private String key;
    private String uploadID;
    private String srcBucket;
    private String srcKey;
    private String srcVersionID;
    private String checkpointFile;
    private CopyPartInfo copyPartInfo;

    public CopyEventType getType() {
        return type;
    }

    public CopyEvent setType(CopyEventType type) {
        this.type = type;
        return this;
    }

    public TosException getException() {
        return exception;
    }

    public CopyEvent setException(TosException exception) {
        this.exception = exception;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public CopyEvent setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CopyEvent setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public CopyEvent setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public CopyEvent setSrcBucket(String srcBucket) {
        this.srcBucket = srcBucket;
        return this;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public CopyEvent setSrcKey(String srcKey) {
        this.srcKey = srcKey;
        return this;
    }

    public String getSrcVersionID() {
        return srcVersionID;
    }

    public CopyEvent setSrcVersionID(String srcVersionID) {
        this.srcVersionID = srcVersionID;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public CopyEvent setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public CopyPartInfo getCopyPartInfo() {
        return copyPartInfo;
    }

    public CopyEvent setCopyPartInfo(CopyPartInfo copyPartInfo) {
        this.copyPartInfo = copyPartInfo;
        return this;
    }

    @Override
    public String toString() {
        return "CopyEvent{" +
                "type=" + type +
                ", exception=" + exception +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", srcBucket='" + srcBucket + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", srcVersionID='" + srcVersionID + '\'' +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", copyPartInfo=" + copyPartInfo +
                '}';
    }
}
