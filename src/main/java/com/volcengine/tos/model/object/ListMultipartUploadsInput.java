package com.volcengine.tos.model.object;


public class ListMultipartUploadsInput {
    private String prefix;
    private String delimiter;
    private String keyMarker;
    private String uploadIDMarker;
    private int maxUploads;

    public String getPrefix() {
        return prefix;
    }

    public ListMultipartUploadsInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListMultipartUploadsInput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListMultipartUploadsInput setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getUploadIDMarker() {
        return uploadIDMarker;
    }

    public ListMultipartUploadsInput setUploadIDMarker(String uploadIDMarker) {
        this.uploadIDMarker = uploadIDMarker;
        return this;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public ListMultipartUploadsInput setMaxUploads(int maxUploads) {
        this.maxUploads = maxUploads;
        return this;
    }
}
