package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class PutBucketPolicyInput extends GenericInput {
    private String bucket;
    private String policy;

    public String getBucket() {
        return bucket;
    }

    public String getPolicy() {
        return policy;
    }

    public PutBucketPolicyInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public PutBucketPolicyInput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketPolicyInput{" +
                "bucket='" + bucket + '\'' +
                ", policy='" + policy + '\'' +
                '}';
    }

    public static PutBucketPolicyInputBuilder builder() {
        return new PutBucketPolicyInputBuilder();
    }

    public static final class PutBucketPolicyInputBuilder {
        private String bucket;
        private String policy;

        private PutBucketPolicyInputBuilder() {
        }

        public PutBucketPolicyInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketPolicyInputBuilder policy(String policy) {
            this.policy = policy;
            return this;
        }

        public PutBucketPolicyInput build() {
            PutBucketPolicyInput putBucketPolicyInput = new PutBucketPolicyInput();
            putBucketPolicyInput.bucket = this.bucket;
            putBucketPolicyInput.policy = this.policy;
            return putBucketPolicyInput;
        }
    }
}
