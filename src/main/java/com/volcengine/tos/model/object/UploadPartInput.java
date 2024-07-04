package com.volcengine.tos.model.object;

import java.io.InputStream;

@Deprecated
public class UploadPartInput {
    private String key;
    private String uploadID;
    private long partSize = -1;
    private int partNumber;
    private InputStream content;

    public UploadPartInput() {
    }

    public UploadPartInput(String key, String uploadID, long partSize, int partNumber, InputStream content) {
        this.key = key;
        this.uploadID = uploadID;
        this.partSize = partSize;
        this.partNumber = partNumber;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public UploadPartInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadPartInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadPartInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartInput setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public UploadPartInput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partSize=" + partSize +
                ", partNumber=" + partNumber +
                ", content=" + content +
                '}';
    }
}
