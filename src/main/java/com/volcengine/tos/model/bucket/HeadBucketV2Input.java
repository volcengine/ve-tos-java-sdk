package com.volcengine.tos.model.bucket;

public class HeadBucketV2Input {
    private String bucket;

    public HeadBucketV2Input() {
    }

    public String getBucket() {
        return bucket;
    }

    public HeadBucketV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "HeadBucketInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static HeadBucketInputBuilder builder() {
        return new HeadBucketInputBuilder();
    }

    public static final class HeadBucketInputBuilder {
        private String bucket;

        private HeadBucketInputBuilder() {
        }

        public HeadBucketInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public HeadBucketV2Input build() {
            HeadBucketV2Input headBucketInput = new HeadBucketV2Input();
            headBucketInput.setBucket(bucket);
            return headBucketInput;
        }
    }
}
