package com.volcengine.tos.model.object;

public class DeleteObjectTaggingInput {
    private String bucket;
    private String key;
    private String versionID;

    public String getBucket() {
        return bucket;
    }

    public DeleteObjectTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DeleteObjectTaggingInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteObjectTaggingInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteObjectTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }

    public static DeleteObjectTaggingInputBuilder builder() {
        return new DeleteObjectTaggingInputBuilder();
    }

    public static final class DeleteObjectTaggingInputBuilder {
        private String bucket;
        private String key;
        private String versionID;

        private DeleteObjectTaggingInputBuilder() {
        }

        public DeleteObjectTaggingInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteObjectTaggingInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public DeleteObjectTaggingInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public DeleteObjectTaggingInput build() {
            DeleteObjectTaggingInput deleteObjectTaggingInput = new DeleteObjectTaggingInput();
            deleteObjectTaggingInput.setBucket(bucket);
            deleteObjectTaggingInput.setKey(key);
            deleteObjectTaggingInput.setVersionID(versionID);
            return deleteObjectTaggingInput;
        }
    }
}
