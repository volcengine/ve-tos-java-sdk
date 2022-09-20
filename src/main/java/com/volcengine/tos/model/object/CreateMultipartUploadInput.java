package com.volcengine.tos.model.object;

import java.util.Map;

public class CreateMultipartUploadInput {
    private String bucket;
    private String key;

    private ObjectMetaRequestOptions options;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    public CreateMultipartUploadInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CreateMultipartUploadInput setKey(String key) {
        this.key = key;
        return this;
    }

    public CreateMultipartUploadInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "CreateMultipartUploadInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", options=" + options +
                '}';
    }

    public static CreateMultipartUploadInputBuilder builder() {
        return new CreateMultipartUploadInputBuilder();
    }

    public static final class CreateMultipartUploadInputBuilder {
        private String bucket;
        private String key;
        private ObjectMetaRequestOptions options;

        private CreateMultipartUploadInputBuilder() {
        }

        public CreateMultipartUploadInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateMultipartUploadInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CreateMultipartUploadInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public CreateMultipartUploadInput build() {
            CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
            createMultipartUploadInput.key = this.key;
            createMultipartUploadInput.bucket = this.bucket;
            createMultipartUploadInput.options = this.options;
            return createMultipartUploadInput;
        }
    }
}