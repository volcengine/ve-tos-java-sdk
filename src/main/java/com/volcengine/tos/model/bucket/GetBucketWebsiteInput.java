package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketWebsiteInput extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketWebsiteInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketWebsiteInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketWebsiteInputBuilder builder() {
        return new GetBucketWebsiteInputBuilder();
    }

    public static final class GetBucketWebsiteInputBuilder {
        private String bucket;

        private GetBucketWebsiteInputBuilder() {
        }

        public GetBucketWebsiteInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketWebsiteInput build() {
            GetBucketWebsiteInput getBucketWebsiteInput = new GetBucketWebsiteInput();
            getBucketWebsiteInput.setBucket(bucket);
            return getBucketWebsiteInput;
        }
    }
}
