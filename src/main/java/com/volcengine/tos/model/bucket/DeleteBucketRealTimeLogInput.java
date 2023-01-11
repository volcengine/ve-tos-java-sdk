package com.volcengine.tos.model.bucket;

public class DeleteBucketRealTimeLogInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketRealTimeLogInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketRealTimeLogInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketRealTimeLogInputBuilder builder() {
        return new DeleteBucketRealTimeLogInputBuilder();
    }

    public static final class DeleteBucketRealTimeLogInputBuilder {
        private String bucket;

        private DeleteBucketRealTimeLogInputBuilder() {
        }

        public DeleteBucketRealTimeLogInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketRealTimeLogInput build() {
            DeleteBucketRealTimeLogInput deleteBucketRealTimeLogInput = new DeleteBucketRealTimeLogInput();
            deleteBucketRealTimeLogInput.setBucket(bucket);
            return deleteBucketRealTimeLogInput;
        }
    }
}
