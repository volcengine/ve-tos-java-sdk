package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class ListPartsInput extends GenericInput {
    private String bucket;
    private String key;
    private String uploadID;
    private int partNumberMarker;
    private int maxParts;
    private String encodingType;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public int getPartNumberMarker() {
        return partNumberMarker;
    }

    public int getMaxParts() {
        return maxParts;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListPartsInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ListPartsInput setKey(String key) {
        this.key = key;
        return this;
    }

    public ListPartsInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public ListPartsInput setPartNumberMarker(int partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
        return this;
    }

    public ListPartsInput setMaxParts(int maxParts) {
        this.maxParts = maxParts;
        return this;
    }

    public ListPartsInput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "ListPartsInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partNumberMarker=" + partNumberMarker +
                ", maxParts=" + maxParts +
                ", encodingType='" + encodingType + '\'' +
                '}';
    }

    public static ListPartsInputBuilder builder() {
        return new ListPartsInputBuilder();
    }

    public static final class ListPartsInputBuilder {
        private String bucket;
        private String key;
        private String uploadID;
        private int partNumberMarker;
        private int maxParts;
        private String encodingType;

        private ListPartsInputBuilder() {
        }

        public ListPartsInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListPartsInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public ListPartsInputBuilder uploadID(String uploadID) {
            this.uploadID = uploadID;
            return this;
        }

        public ListPartsInputBuilder partNumberMarker(int partNumberMarker) {
            this.partNumberMarker = partNumberMarker;
            return this;
        }

        public ListPartsInputBuilder maxParts(int maxParts) {
            this.maxParts = maxParts;
            return this;
        }

        public ListPartsInputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListPartsInput build() {
            ListPartsInput listPartsInput = new ListPartsInput();
            listPartsInput.bucket = this.bucket;
            listPartsInput.partNumberMarker = this.partNumberMarker;
            listPartsInput.encodingType = this.encodingType;
            listPartsInput.key = this.key;
            listPartsInput.uploadID = this.uploadID;
            listPartsInput.maxParts = this.maxParts;
            return listPartsInput;
        }
    }
}
