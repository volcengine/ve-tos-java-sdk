package com.volcengine.tos.model.object;

public class ListObjectsType2Input {
    private String bucket;
    private String prefix;
    private String delimiter;
    private String startAfter;
    private String continuationToken;
    private int maxKeys;
    private String encodingType;
    private boolean listOnlyOnce;

    public String getBucket() {
        return bucket;
    }

    public ListObjectsType2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectsType2Input setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectsType2Input setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public ListObjectsType2Input setStartAfter(String startAfter) {
        this.startAfter = startAfter;
        return this;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public ListObjectsType2Input setContinuationToken(String continuationToken) {
        this.continuationToken = continuationToken;
        return this;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public ListObjectsType2Input setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsType2Input setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public boolean isListOnlyOnce() {
        return listOnlyOnce;
    }

    public ListObjectsType2Input setListOnlyOnce(boolean listOnlyOnce) {
        this.listOnlyOnce = listOnlyOnce;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsType2Input{" +
                "bucket='" + bucket + '\'' +
                ", prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", startAfter='" + startAfter + '\'' +
                ", continuationToken='" + continuationToken + '\'' +
                ", maxKeys=" + maxKeys +
                ", encodingType='" + encodingType + '\'' +
                ", listOnlyOnce=" + listOnlyOnce +
                '}';
    }

    public static ListObjectsType2InputBuilder builder() {
        return new ListObjectsType2InputBuilder();
    }

    public static final class ListObjectsType2InputBuilder {
        private String bucket;
        private String prefix;
        private String delimiter;
        private String startAfter;
        private String continuationToken;
        private int maxKeys;
        private String encodingType;
        private boolean listOnlyOnce;

        private ListObjectsType2InputBuilder() {
        }

        public ListObjectsType2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListObjectsType2InputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListObjectsType2InputBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public ListObjectsType2InputBuilder startAfter(String startAfter) {
            this.startAfter = startAfter;
            return this;
        }

        public ListObjectsType2InputBuilder continuationToken(String continuationToken) {
            this.continuationToken = continuationToken;
            return this;
        }

        public ListObjectsType2InputBuilder maxKeys(int maxKeys) {
            this.maxKeys = maxKeys;
            return this;
        }

        public ListObjectsType2InputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListObjectsType2InputBuilder listOnlyOnce(boolean listOnlyOnce) {
            this.listOnlyOnce = listOnlyOnce;
            return this;
        }

        public ListObjectsType2Input build() {
            ListObjectsType2Input listObjectsType2Input = new ListObjectsType2Input();
            listObjectsType2Input.setBucket(bucket);
            listObjectsType2Input.setPrefix(prefix);
            listObjectsType2Input.setDelimiter(delimiter);
            listObjectsType2Input.setStartAfter(startAfter);
            listObjectsType2Input.setContinuationToken(continuationToken);
            listObjectsType2Input.setMaxKeys(maxKeys);
            listObjectsType2Input.setEncodingType(encodingType);
            listObjectsType2Input.setListOnlyOnce(listOnlyOnce);
            return listObjectsType2Input;
        }
    }
}
