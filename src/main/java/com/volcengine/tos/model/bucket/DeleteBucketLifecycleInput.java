package com.volcengine.tos.model.bucket;

public class DeleteBucketLifecycleInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketLifecycleInputBuilder builder() {
        return new DeleteBucketLifecycleInputBuilder();
    }

    public static final class DeleteBucketLifecycleInputBuilder {
        private String bucket;

        private DeleteBucketLifecycleInputBuilder() {
        }

        public DeleteBucketLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketLifecycleInput build() {
            DeleteBucketLifecycleInput deleteBucketLifecycleInput = new DeleteBucketLifecycleInput();
            deleteBucketLifecycleInput.setBucket(bucket);
            return deleteBucketLifecycleInput;
        }
    }
}
