package com.volcengine.tos.model.bucket;

public class GetBucketVersioningInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketVersioningInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketVersioningInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketVersioningInputBuilder builder() {
        return new GetBucketVersioningInputBuilder();
    }

    public static final class GetBucketVersioningInputBuilder {
        private String bucket;

        private GetBucketVersioningInputBuilder() {
        }

        public GetBucketVersioningInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketVersioningInput build() {
            GetBucketVersioningInput getBucketVersioningInput = new GetBucketVersioningInput();
            getBucketVersioningInput.setBucket(bucket);
            return getBucketVersioningInput;
        }
    }
}
