package com.volcengine.tos.model.bucket;

public class GetBucketRealTimeLogInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketRealTimeLogInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketRealTimeLogInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketRealTimeLogInputBuilder builder() {
        return new GetBucketRealTimeLogInputBuilder();
    }

    public static final class GetBucketRealTimeLogInputBuilder {
        private String bucket;

        private GetBucketRealTimeLogInputBuilder() {
        }

        public GetBucketRealTimeLogInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketRealTimeLogInput build() {
            GetBucketRealTimeLogInput getBucketRealTimeLogInput = new GetBucketRealTimeLogInput();
            getBucketRealTimeLogInput.setBucket(bucket);
            return getBucketRealTimeLogInput;
        }
    }
}
