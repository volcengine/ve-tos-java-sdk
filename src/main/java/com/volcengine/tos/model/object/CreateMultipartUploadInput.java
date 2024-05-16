package com.volcengine.tos.model.object;

import java.util.Map;

public class CreateMultipartUploadInput {
    private String bucket;
    private String key;
    private String encodingType;

    private ObjectMetaRequestOptions options;
    private boolean forbidOverwrite;

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

    public String getEncodingType() {
        return encodingType;
    }

    public CreateMultipartUploadInput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public CreateMultipartUploadInput setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    @Override
    public String toString() {
        return "CreateMultipartUploadInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", options=" + options +
                ", forbidOverwrite=" + forbidOverwrite +
                '}';
    }

    public static CreateMultipartUploadInputBuilder builder() {
        return new CreateMultipartUploadInputBuilder();
    }

    public static final class CreateMultipartUploadInputBuilder {
        private String bucket;
        private String key;
        private String encodingType;
        private ObjectMetaRequestOptions options;
        private boolean forbidOverwrite;

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

        public CreateMultipartUploadInputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public CreateMultipartUploadInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public CreateMultipartUploadInputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.forbidOverwrite = forbidOverwrite;
            return this;
        }

        public CreateMultipartUploadInput build() {
            CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
            createMultipartUploadInput.setBucket(bucket);
            createMultipartUploadInput.setKey(key);
            createMultipartUploadInput.setEncodingType(encodingType);
            createMultipartUploadInput.setOptions(options);
            createMultipartUploadInput.setForbidOverwrite(forbidOverwrite);
            return createMultipartUploadInput;
        }
    }
}
