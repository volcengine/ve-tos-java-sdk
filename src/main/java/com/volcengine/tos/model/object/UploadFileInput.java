package com.volcengine.tos.model.object;

public class UploadFileInput {
    private String objectKey;
    private String uploadFilePath;
    private long partSize;
    private int taskNum;
    private boolean enableCheckpoint;
    private String checkpointFile;

    public String getObjectKey() {
        return objectKey;
    }

    public UploadFileInput setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public UploadFileInput setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadFileInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public UploadFileInput setTaskNum(int taskNum) {
        this.taskNum = taskNum;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public UploadFileInput setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public UploadFileInput setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }
}
