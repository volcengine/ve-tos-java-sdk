package com.volcengine.tos.model.object;

public class ListObjectVersionsV2Input {
    private String bucket;
    private String prefix;
    private String delimiter;
    private String keyMarker;
    private String versionIDMarker;
    private int maxKeys;
    private String encodingType;

    public String getBucket() {
        return bucket;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public String getVersionIDMarker() {
        return versionIDMarker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectVersionsV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ListObjectVersionsV2Input setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ListObjectVersionsV2Input setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public ListObjectVersionsV2Input setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public ListObjectVersionsV2Input setVersionIDMarker(String versionIDMarker) {
        this.versionIDMarker = versionIDMarker;
        return this;
    }

    public ListObjectVersionsV2Input setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public ListObjectVersionsV2Input setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectVersionsV2Input{" +
                "bucket='" + bucket + '\'' +
                ", prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", versionIDMarker='" + versionIDMarker + '\'' +
                ", maxKeys=" + maxKeys +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }

    public static ListObjectVersionsV2InputBuilder builder() {
        return new ListObjectVersionsV2InputBuilder();
    }

    public static final class ListObjectVersionsV2InputBuilder {
        private String bucket;
        private String prefix;
        private String delimiter;
        private String keyMarker;
        private String versionIDMarker;
        private int maxKeys;
        private String encodingType;

        private ListObjectVersionsV2InputBuilder() {
        }

        public ListObjectVersionsV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListObjectVersionsV2InputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListObjectVersionsV2InputBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public ListObjectVersionsV2InputBuilder keyMarker(String keyMarker) {
            this.keyMarker = keyMarker;
            return this;
        }

        public ListObjectVersionsV2InputBuilder versionIDMarker(String versionIDMarker) {
            this.versionIDMarker = versionIDMarker;
            return this;
        }

        public ListObjectVersionsV2InputBuilder maxKeys(int maxKeys) {
            this.maxKeys = maxKeys;
            return this;
        }

        public ListObjectVersionsV2InputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListObjectVersionsV2Input build() {
            ListObjectVersionsV2Input listObjectVersionsV2Input = new ListObjectVersionsV2Input();
            listObjectVersionsV2Input.keyMarker = this.keyMarker;
            listObjectVersionsV2Input.versionIDMarker = this.versionIDMarker;
            listObjectVersionsV2Input.encodingType = this.encodingType;
            listObjectVersionsV2Input.prefix = this.prefix;
            listObjectVersionsV2Input.bucket = this.bucket;
            listObjectVersionsV2Input.delimiter = this.delimiter;
            listObjectVersionsV2Input.maxKeys = this.maxKeys;
            return listObjectVersionsV2Input;
        }
    }
}
