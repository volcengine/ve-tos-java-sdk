package com.volcengine.tos.model.object;

public class DeleteObjectInput {
    private String bucket;
    private String key;
    private String versionID;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public DeleteObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public DeleteObjectInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String bucket;
        private String key;
        private String versionID;

        private Builder() {
        }

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public DeleteObjectInput build() {
            DeleteObjectInput deleteObjectInput = new DeleteObjectInput();
            deleteObjectInput.bucket = this.bucket;
            deleteObjectInput.versionID = this.versionID;
            deleteObjectInput.key = this.key;
            return deleteObjectInput;
        }
    }
}
