package com.volcengine.tos.model.bucket;

public class GetBucketLifecycleInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketLifecycleInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketLifecycleInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketLifecycleInputBuilder builder() {
        return new GetBucketLifecycleInputBuilder();
    }

    public static final class GetBucketLifecycleInputBuilder {
        private String bucket;

        private GetBucketLifecycleInputBuilder() {
        }

        public GetBucketLifecycleInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketLifecycleInput build() {
            GetBucketLifecycleInput getBucketLifecycleInput = new GetBucketLifecycleInput();
            getBucketLifecycleInput.setBucket(bucket);
            return getBucketLifecycleInput;
        }
    }
}
