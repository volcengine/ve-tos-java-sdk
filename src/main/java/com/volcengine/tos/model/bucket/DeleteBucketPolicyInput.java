package com.volcengine.tos.model.bucket;

public class DeleteBucketPolicyInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketPolicyInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketPolicyInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketPolicyInputBuilder builder() {
        return new DeleteBucketPolicyInputBuilder();
    }

    public static final class DeleteBucketPolicyInputBuilder {
        private String bucket;

        private DeleteBucketPolicyInputBuilder() {
        }

        public DeleteBucketPolicyInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketPolicyInput build() {
            DeleteBucketPolicyInput deleteBucketPolicyInput = new DeleteBucketPolicyInput();
            deleteBucketPolicyInput.bucket = this.bucket;
            return deleteBucketPolicyInput;
        }
    }
}
