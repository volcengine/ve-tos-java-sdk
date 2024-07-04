package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class ListObjectsV2Input extends GenericInput {
    private String bucket;
    private String prefix;
    private String delimiter;
    private String marker;
    private int maxKeys;
    private boolean reverse;
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

    public String getMarker() {
        return marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public boolean isReverse() {
        return reverse;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ListObjectsV2Input setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ListObjectsV2Input setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public ListObjectsV2Input setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public ListObjectsV2Input setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public ListObjectsV2Input setReverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public ListObjectsV2Input setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsV2Input{" +
                "bucket='" + bucket + '\'' +
                ", prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", marker='" + marker + '\'' +
                ", maxKeys=" + maxKeys +
                ", reverse=" + reverse +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }

    public static ListObjectsV2InputBuilder builder() {
        return new ListObjectsV2InputBuilder();
    }

    public static final class ListObjectsV2InputBuilder {
        private String bucket;
        private String prefix;
        private String delimiter;
        private String marker;
        private int maxKeys;
        private boolean reverse;
        private String encodingType;

        private ListObjectsV2InputBuilder() {
        }

        public ListObjectsV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListObjectsV2InputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListObjectsV2InputBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public ListObjectsV2InputBuilder marker(String marker) {
            this.marker = marker;
            return this;
        }

        public ListObjectsV2InputBuilder maxKeys(int maxKeys) {
            this.maxKeys = maxKeys;
            return this;
        }

        public ListObjectsV2InputBuilder reverse(boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public ListObjectsV2InputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListObjectsV2Input build() {
            ListObjectsV2Input listObjectsV2Input = new ListObjectsV2Input();
            listObjectsV2Input.bucket = this.bucket;
            listObjectsV2Input.delimiter = this.delimiter;
            listObjectsV2Input.reverse = this.reverse;
            listObjectsV2Input.encodingType = this.encodingType;
            listObjectsV2Input.maxKeys = this.maxKeys;
            listObjectsV2Input.marker = this.marker;
            listObjectsV2Input.prefix = this.prefix;
            return listObjectsV2Input;
        }
    }
}
