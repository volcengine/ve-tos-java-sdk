package com.volcengine.tos.model.object;

@Deprecated
public class ListObjectVersionsInput {
    private String prefix;
    private String delimiter;
    private String keyMarker;
    private String versionIDMarker;
    private int maxKeys;
    private String encodingType;

    public String getPrefix() {
        return prefix;
    }

    public ListObjectVersionsInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectVersionsInput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListObjectVersionsInput setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getVersionIDMarker() {
        return versionIDMarker;
    }

    public ListObjectVersionsInput setVersionIDMarker(String versionIDMarker) {
        this.versionIDMarker = versionIDMarker;
        return this;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public ListObjectVersionsInput setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectVersionsInput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectVersionsInput{" +
                "prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", versionIDMarker='" + versionIDMarker + '\'' +
                ", maxKeys=" + maxKeys +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }
}
