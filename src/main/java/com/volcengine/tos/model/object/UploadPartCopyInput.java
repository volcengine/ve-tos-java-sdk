package com.volcengine.tos.model.object;


public class UploadPartCopyInput {
    private String uploadID;
    private String destinationKey;
    private String sourceBucket;
    private String sourceKey;
    private String sourceVersionID;
    private long startOffset;
    private long partSize;
    private int partNumber;

    public String getUploadID() {
        return uploadID;
    }

    public UploadPartCopyInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getDestinationKey() {
        return destinationKey;
    }

    public UploadPartCopyInput setDestinationKey(String destinationKey) {
        this.destinationKey = destinationKey;
        return this;
    }

    public String getSourceBucket() {
        return sourceBucket;
    }

    public UploadPartCopyInput setSourceBucket(String sourceBucket) {
        this.sourceBucket = sourceBucket;
        return this;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public UploadPartCopyInput setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
        return this;
    }

    public String getSourceVersionID() {
        return sourceVersionID;
    }

    public UploadPartCopyInput setSourceVersionID(String sourceVersionID) {
        this.sourceVersionID = sourceVersionID;
        return this;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public UploadPartCopyInput setStartOffset(long startOffset) {
        this.startOffset = startOffset;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadPartCopyInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public UploadPartCopyInput setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartCopyInput{" +
                "uploadID='" + uploadID + '\'' +
                ", destinationKey='" + destinationKey + '\'' +
                ", sourceBucket='" + sourceBucket + '\'' +
                ", sourceKey='" + sourceKey + '\'' +
                ", sourceVersionID='" + sourceVersionID + '\'' +
                ", startOffset=" + startOffset +
                ", partSize=" + partSize +
                ", partNumber=" + partNumber +
                '}';
    }
}
