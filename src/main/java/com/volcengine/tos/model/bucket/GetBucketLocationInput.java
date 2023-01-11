package com.volcengine.tos.model.bucket;

public class GetBucketLocationInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketLocationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketLocationInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketLocationInputBuilder builder() {
        return new GetBucketLocationInputBuilder();
    }

    public static final class GetBucketLocationInputBuilder {
        private String bucket;

        private GetBucketLocationInputBuilder() {
        }

        public GetBucketLocationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketLocationInput build() {
            GetBucketLocationInput getBucketLocationInput = new GetBucketLocationInput();
            getBucketLocationInput.setBucket(bucket);
            return getBucketLocationInput;
        }
    }
}
