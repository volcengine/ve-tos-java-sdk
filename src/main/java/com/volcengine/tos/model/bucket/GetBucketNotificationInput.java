package com.volcengine.tos.model.bucket;

public class GetBucketNotificationInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketNotificationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketNotificationInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketNotificationInputBuilder builder() {
        return new GetBucketNotificationInputBuilder();
    }

    public static final class GetBucketNotificationInputBuilder {
        private String bucket;

        private GetBucketNotificationInputBuilder() {
        }

        public GetBucketNotificationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketNotificationInput build() {
            GetBucketNotificationInput getBucketNotificationInput = new GetBucketNotificationInput();
            getBucketNotificationInput.setBucket(bucket);
            return getBucketNotificationInput;
        }
    }
}
