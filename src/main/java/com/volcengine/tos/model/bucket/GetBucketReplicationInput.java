package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetBucketReplicationInput extends GenericInput {
    private String bucket;
    private String ruleID;

    public String getBucket() {
        return bucket;
    }

    public GetBucketReplicationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getRuleID() {
        return ruleID;
    }

    public GetBucketReplicationInput setRuleID(String ruleID) {
        this.ruleID = ruleID;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketReplicationInput{" +
                "bucket='" + bucket + '\'' +
                ", ruleID='" + ruleID + '\'' +
                '}';
    }

    public static GetBucketReplicationInputBuilder builder() {
        return new GetBucketReplicationInputBuilder();
    }

    public static final class GetBucketReplicationInputBuilder {
        private String bucket;
        private String ruleID;

        private GetBucketReplicationInputBuilder() {
        }

        public GetBucketReplicationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketReplicationInputBuilder ruleID(String ruleID) {
            this.ruleID = ruleID;
            return this;
        }

        public GetBucketReplicationInput build() {
            GetBucketReplicationInput getBucketReplicationInput = new GetBucketReplicationInput();
            getBucketReplicationInput.setBucket(bucket);
            getBucketReplicationInput.setRuleID(ruleID);
            return getBucketReplicationInput;
        }
    }
}
