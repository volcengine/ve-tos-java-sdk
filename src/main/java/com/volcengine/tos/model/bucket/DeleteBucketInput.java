package com.volcengine.tos.model.bucket;

public class DeleteBucketInput {
    private String bucket;

    public DeleteBucketInput() {
    }

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public static DeleteBucketInputBuilder builder() {
        return new DeleteBucketInputBuilder();
    }

    @Override
    public String toString() {
        return "DeleteBucketInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static final class DeleteBucketInputBuilder {
        private String bucket;

        private DeleteBucketInputBuilder() {
        }

        public DeleteBucketInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketInput build() {
            DeleteBucketInput deleteBucketInput = new DeleteBucketInput();
            deleteBucketInput.bucket = this.bucket;
            return deleteBucketInput;
        }
    }
}
