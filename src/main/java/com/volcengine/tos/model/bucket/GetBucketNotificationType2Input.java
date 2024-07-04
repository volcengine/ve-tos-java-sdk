package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketNotificationType2Input extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketNotificationType2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketNotificationType2Input{" +
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

        public GetBucketNotificationType2Input build() {
            GetBucketNotificationType2Input getBucketNotificationInput = new GetBucketNotificationType2Input();
            getBucketNotificationInput.setBucket(bucket);
            return getBucketNotificationInput;
        }
    }
}
