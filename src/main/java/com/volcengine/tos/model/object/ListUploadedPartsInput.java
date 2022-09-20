package com.volcengine.tos.model.object;

@Deprecated
public class ListUploadedPartsInput {
    private String key;
    private String uploadID;
    private int maxParts;
    private int partNumberMarker;

    public String getKey() {
        return key;
    }

    public ListUploadedPartsInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ListUploadedPartsInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public int getMaxParts() {
        return maxParts;
    }

    public ListUploadedPartsInput setMaxParts(int maxParts) {
        this.maxParts = maxParts;
        return this;
    }

    public int getPartNumberMarker() {
        return partNumberMarker;
    }

    public ListUploadedPartsInput setPartNumberMarker(int partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
        return this;
    }

    @Override
    public String toString() {
        return "ListUploadedPartsInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", maxParts=" + maxParts +
                ", partNumberMarker=" + partNumberMarker +
                '}';
    }
}
