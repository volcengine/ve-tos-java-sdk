package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.VersioningStatusType;

public class PutBucketVersioningInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Status")
    private VersioningStatusType status;

    public String getBucket() {
        return bucket;
    }

    public PutBucketVersioningInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public VersioningStatusType getStatus() {
        return status;
    }

    public PutBucketVersioningInput setStatus(VersioningStatusType status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketVersioningInput{" +
                "bucket='" + bucket + '\'' +
                ", status=" + status +
                '}';
    }

    public static PutBucketVersioningInputBuilder builder() {
        return new PutBucketVersioningInputBuilder();
    }

    public static final class PutBucketVersioningInputBuilder {
        private String bucket;
        private VersioningStatusType status;

        private PutBucketVersioningInputBuilder() {
        }

        public PutBucketVersioningInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketVersioningInputBuilder status(VersioningStatusType status) {
            this.status = status;
            return this;
        }

        public PutBucketVersioningInput build() {
            PutBucketVersioningInput putBucketVersioningInput = new PutBucketVersioningInput();
            putBucketVersioningInput.setBucket(bucket);
            putBucketVersioningInput.setStatus(status);
            return putBucketVersioningInput;
        }
    }
}
