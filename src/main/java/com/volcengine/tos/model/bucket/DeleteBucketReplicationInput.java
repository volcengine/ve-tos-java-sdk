package com.volcengine.tos.model.bucket;

public class DeleteBucketReplicationInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketReplicationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketReplicationInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketReplicationInputBuilder builder() {
        return new DeleteBucketReplicationInputBuilder();
    }

    public static final class DeleteBucketReplicationInputBuilder {
        private String bucket;

        private DeleteBucketReplicationInputBuilder() {
        }

        public DeleteBucketReplicationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketReplicationInput build() {
            DeleteBucketReplicationInput deleteBucketReplicationInput = new DeleteBucketReplicationInput();
            deleteBucketReplicationInput.setBucket(bucket);
            return deleteBucketReplicationInput;
        }
    }
}
