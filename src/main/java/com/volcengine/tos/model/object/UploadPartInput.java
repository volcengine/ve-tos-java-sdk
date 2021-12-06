package com.volcengine.tos.model.object;


public class UploadPartInput {
    private String key;
    private String uploadID;
    private long partSize;
    private int partNumber;
    private TosObjectInputStream content;

    public UploadPartInput(String key, String uploadID, long partSize, int partNumber, TosObjectInputStream content) {
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

    public TosObjectInputStream getContent() {
        return content;
    }

    public UploadPartInput setContent(TosObjectInputStream content) {
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
