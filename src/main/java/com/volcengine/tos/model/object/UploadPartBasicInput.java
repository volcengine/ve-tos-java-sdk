package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;

import java.util.Map;

public class UploadPartBasicInput {
    private String bucket;
    private String key;
    private String uploadID;
    private int partNumber;

    private ObjectMetaRequestOptions options;

    private DataTransferListener dataTransferListener;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public UploadPartBasicInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public UploadPartBasicInput setKey(String key) {
        this.key = key;
        return this;
    }

    public UploadPartBasicInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public UploadPartBasicInput setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public UploadPartBasicInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public UploadPartBasicInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    @Override
    public String toString() {
        return "UploadPartBasicInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partNumber=" + partNumber +
                ", options=" + options +
                ", dataTransferListener=" + dataTransferListener +
                '}';
    }

    public static UploadPartBasicInputBuilder builder() {
        return new UploadPartBasicInputBuilder();
    }

    public static final class UploadPartBasicInputBuilder {
        private String bucket;
        private String key;
        private String uploadID;
        private int partNumber;
        private ObjectMetaRequestOptions options;
        private DataTransferListener dataTransferListener;

        private UploadPartBasicInputBuilder() {
        }

        public UploadPartBasicInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public UploadPartBasicInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public UploadPartBasicInputBuilder uploadID(String uploadID) {
            this.uploadID = uploadID;
            return this;
        }

        public UploadPartBasicInputBuilder partNumber(int partNumber) {
            this.partNumber = partNumber;
            return this;
        }

        public UploadPartBasicInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public UploadPartBasicInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public UploadPartBasicInput build() {
            UploadPartBasicInput uploadPartBasicInput = new UploadPartBasicInput();
            uploadPartBasicInput.bucket = this.bucket;
            uploadPartBasicInput.dataTransferListener = this.dataTransferListener;
            uploadPartBasicInput.partNumber = this.partNumber;
            uploadPartBasicInput.key = this.key;
            uploadPartBasicInput.uploadID = this.uploadID;
            uploadPartBasicInput.options = this.options;
            return uploadPartBasicInput;
        }
    }
}