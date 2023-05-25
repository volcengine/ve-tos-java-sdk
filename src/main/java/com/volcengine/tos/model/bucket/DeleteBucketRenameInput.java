package com.volcengine.tos.model.bucket;

public class DeleteBucketRenameInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketRenameInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketRenameInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketRenameInputBuilder builder() {
        return new DeleteBucketRenameInputBuilder();
    }

    public static final class DeleteBucketRenameInputBuilder {
        private String bucket;

        private DeleteBucketRenameInputBuilder() {
        }

        public DeleteBucketRenameInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketRenameInput build() {
            DeleteBucketRenameInput deleteBucketRenameInput = new DeleteBucketRenameInput();
            deleteBucketRenameInput.setBucket(bucket);
            return deleteBucketRenameInput;
        }
    }
}
