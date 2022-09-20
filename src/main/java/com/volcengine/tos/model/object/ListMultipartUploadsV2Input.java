package com.volcengine.tos.model.object;

public class ListMultipartUploadsV2Input {
    private String bucket;
    private String encodingType;
    private String prefix;
    private String delimiter;
    private String keyMarker;
    private String uploadIDMarker;
    private int maxUploads;

    public String getBucket() {
        return bucket;
    }

    public String getEncodingType() {
        return encodingType;
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

    public String getUploadIDMarker() {
        return uploadIDMarker;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public ListMultipartUploadsV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ListMultipartUploadsV2Input setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public ListMultipartUploadsV2Input setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ListMultipartUploadsV2Input setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public ListMultipartUploadsV2Input setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public ListMultipartUploadsV2Input setUploadIDMarker(String uploadIDMarker) {
        this.uploadIDMarker = uploadIDMarker;
        return this;
    }

    public ListMultipartUploadsV2Input setMaxUploads(int maxUploads) {
        this.maxUploads = maxUploads;
        return this;
    }

    @Override
    public String toString() {
        return "ListMultipartUploadsV2Input{" +
                "bucket='" + bucket + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", uploadIDMarker='" + uploadIDMarker + '\'' +
                ", maxUploads=" + maxUploads +
                '}';
    }

    public static ListMultipartUploadsV2InputBuilder builder() {
        return new ListMultipartUploadsV2InputBuilder();
    }

    public static final class ListMultipartUploadsV2InputBuilder {
        private String bucket;
        private String encodingType;
        private String prefix;
        private String delimiter;
        private String keyMarker;
        private String uploadIDMarker;
        private int maxUploads;

        private ListMultipartUploadsV2InputBuilder() {
        }

        public ListMultipartUploadsV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder keyMarker(String keyMarker) {
            this.keyMarker = keyMarker;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder uploadIDMarker(String uploadIDMarker) {
            this.uploadIDMarker = uploadIDMarker;
            return this;
        }

        public ListMultipartUploadsV2InputBuilder maxUploads(int maxUploads) {
            this.maxUploads = maxUploads;
            return this;
        }

        public ListMultipartUploadsV2Input build() {
            ListMultipartUploadsV2Input listMultipartUploadsV2Input = new ListMultipartUploadsV2Input();
            listMultipartUploadsV2Input.encodingType = this.encodingType;
            listMultipartUploadsV2Input.delimiter = this.delimiter;
            listMultipartUploadsV2Input.keyMarker = this.keyMarker;
            listMultipartUploadsV2Input.maxUploads = this.maxUploads;
            listMultipartUploadsV2Input.bucket = this.bucket;
            listMultipartUploadsV2Input.uploadIDMarker = this.uploadIDMarker;
            listMultipartUploadsV2Input.prefix = this.prefix;
            return listMultipartUploadsV2Input;
        }
    }
}
