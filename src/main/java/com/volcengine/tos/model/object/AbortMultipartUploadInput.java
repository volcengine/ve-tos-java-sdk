package com.volcengine.tos.model.object;


import com.volcengine.tos.model.GenericInput;

public class AbortMultipartUploadInput extends GenericInput {
    private String bucket;
    private String key;
    private String uploadID;

    public AbortMultipartUploadInput() {
    }

    public String getBucket() {
        return bucket;
    }

    public AbortMultipartUploadInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public AbortMultipartUploadInput(String key, String uploadID) {
        this.key = key;
        this.uploadID = uploadID;
    }

    public String getKey() {
        return key;
    }

    public AbortMultipartUploadInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public AbortMultipartUploadInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                '}';
    }

    public static AbortMultipartUploadInputBuilder builder() {
        return new AbortMultipartUploadInputBuilder();
    }

    public static final class AbortMultipartUploadInputBuilder {
        private String bucket;
        private String key;
        private String uploadID;

        private AbortMultipartUploadInputBuilder() {
        }

        public AbortMultipartUploadInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public AbortMultipartUploadInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public AbortMultipartUploadInputBuilder uploadID(String uploadID) {
            this.uploadID = uploadID;
            return this;
        }

        public AbortMultipartUploadInput build() {
            AbortMultipartUploadInput abortMultipartUploadInput = new AbortMultipartUploadInput();
            abortMultipartUploadInput.setBucket(bucket);
            abortMultipartUploadInput.setKey(key);
            abortMultipartUploadInput.setUploadID(uploadID);
            return abortMultipartUploadInput;
        }
    }
}
