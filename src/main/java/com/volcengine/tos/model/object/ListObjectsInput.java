package com.volcengine.tos.model.object;


public class ListObjectsInput {
    private String prefix;
    private String delimiter;
    private String marker;
    private int maxKeys;
    private boolean reverse;
    private String encodingType;

    public String getPrefix() {
        return prefix;
    }

    public ListObjectsInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectsInput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getMarker() {
        return marker;
    }

    public ListObjectsInput setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public ListObjectsInput setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public boolean isReverse() {
        return reverse;
    }

    public ListObjectsInput setReverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsInput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsInput{" +
                "prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", marker='" + marker + '\'' +
                ", maxKeys=" + maxKeys +
                ", reverse=" + reverse +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }
}
