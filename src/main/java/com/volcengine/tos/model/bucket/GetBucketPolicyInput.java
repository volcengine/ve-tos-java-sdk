package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketPolicyInput extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketPolicyInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketPolicyInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketPolicyInputBuilder builder() {
        return new GetBucketPolicyInputBuilder();
    }

    public static final class GetBucketPolicyInputBuilder {
        private String bucket;

        private GetBucketPolicyInputBuilder() {
        }

        public GetBucketPolicyInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketPolicyInput build() {
            GetBucketPolicyInput getBucketPolicyInput = new GetBucketPolicyInput();
            getBucketPolicyInput.bucket = this.bucket;
            return getBucketPolicyInput;
        }
    }
}
