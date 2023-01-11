package com.volcengine.tos.model.bucket;

public class DeleteBucketMirrorBackInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketMirrorBackInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketMirrorBackInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketMirrorBackInputBuilder builder() {
        return new DeleteBucketMirrorBackInputBuilder();
    }

    public static final class DeleteBucketMirrorBackInputBuilder {
        private String bucket;

        private DeleteBucketMirrorBackInputBuilder() {
        }

        public DeleteBucketMirrorBackInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketMirrorBackInput build() {
            DeleteBucketMirrorBackInput deleteBucketMirrorBackInput = new DeleteBucketMirrorBackInput();
            deleteBucketMirrorBackInput.setBucket(bucket);
            return deleteBucketMirrorBackInput;
        }
    }
}
