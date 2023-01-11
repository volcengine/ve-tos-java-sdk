package com.volcengine.tos.model.bucket;

public class DeleteBucketCORSInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketCORSInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketCORSInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketCORSInputBuilder builder() {
        return new DeleteBucketCORSInputBuilder();
    }

    public static final class DeleteBucketCORSInputBuilder {
        private String bucket;

        private DeleteBucketCORSInputBuilder() {
        }

        public DeleteBucketCORSInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketCORSInput build() {
            DeleteBucketCORSInput deleteBucketCORSInput = new DeleteBucketCORSInput();
            deleteBucketCORSInput.setBucket(bucket);
            return deleteBucketCORSInput;
        }
    }
}
