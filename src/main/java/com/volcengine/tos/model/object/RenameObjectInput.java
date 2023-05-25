package com.volcengine.tos.model.object;

public class RenameObjectInput {
    private String bucket;
    private String key;
    private String newKey;

    public String getBucket() {
        return bucket;
    }

    public RenameObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public RenameObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getNewKey() {
        return newKey;
    }

    public RenameObjectInput setNewKey(String newKey) {
        this.newKey = newKey;
        return this;
    }

    @Override
    public String toString() {
        return "RenameObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", newKey='" + newKey + '\'' +
                '}';
    }

    public static RenameObjectInputBuilder builder() {
        return new RenameObjectInputBuilder();
    }

    public static final class RenameObjectInputBuilder {
        private String bucket;
        private String key;
        private String newKey;

        private RenameObjectInputBuilder() {
        }

        public RenameObjectInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public RenameObjectInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public RenameObjectInputBuilder newKey(String newKey) {
            this.newKey = newKey;
            return this;
        }

        public RenameObjectInput build() {
            RenameObjectInput renameObjectInput = new RenameObjectInput();
            renameObjectInput.setBucket(bucket);
            renameObjectInput.setKey(key);
            renameObjectInput.setNewKey(newKey);
            return renameObjectInput;
        }
    }
}
