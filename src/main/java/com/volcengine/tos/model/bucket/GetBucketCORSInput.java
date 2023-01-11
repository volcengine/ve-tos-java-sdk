package com.volcengine.tos.model.bucket;

public class GetBucketCORSInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketCORSInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketCORSInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketCORSInputBuilder builder() {
        return new GetBucketCORSInputBuilder();
    }

    public static final class GetBucketCORSInputBuilder {
        private String bucket;

        private GetBucketCORSInputBuilder() {
        }

        public GetBucketCORSInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketCORSInput build() {
            GetBucketCORSInput getBucketCORSInput = new GetBucketCORSInput();
            getBucketCORSInput.setBucket(bucket);
            return getBucketCORSInput;
        }
    }
}
