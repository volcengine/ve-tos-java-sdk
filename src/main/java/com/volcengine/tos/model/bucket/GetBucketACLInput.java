package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketACLInput extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketACLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketACLInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketACLInputBuilder builder() {
        return new GetBucketACLInputBuilder();
    }

    public static final class GetBucketACLInputBuilder {
        private String bucket;

        private GetBucketACLInputBuilder() {
        }

        public GetBucketACLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketACLInput build() {
            GetBucketACLInput getBucketACLInput = new GetBucketACLInput();
            getBucketACLInput.setBucket(bucket);
            return getBucketACLInput;
        }
    }
}
