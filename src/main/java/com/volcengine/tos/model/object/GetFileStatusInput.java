package com.volcengine.tos.model.object;

public class GetFileStatusInput {
    private String bucket;
    private String key;

    public String getBucket() {
        return bucket;
    }

    public GetFileStatusInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetFileStatusInput setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public String toString() {
        return "GetFileStatusInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public static GetFileStatusInputBuilder builder() {
        return new GetFileStatusInputBuilder();
    }

    public static final class GetFileStatusInputBuilder {
        private String bucket;
        private String key;

        private GetFileStatusInputBuilder() {
        }

        public GetFileStatusInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetFileStatusInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public GetFileStatusInput build() {
            GetFileStatusInput getFileStatusInput = new GetFileStatusInput();
            getFileStatusInput.setBucket(bucket);
            getFileStatusInput.setKey(key);
            return getFileStatusInput;
        }
    }
}
