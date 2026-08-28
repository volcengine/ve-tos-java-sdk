package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.model.GenericInput;

public class PutBucketAccessMonitorInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Status")
    private StatusType status;

    public PutBucketAccessMonitorInput() {
    }

    public PutBucketAccessMonitorInput(String bucket, StatusType status) {
        this.bucket = bucket;
        this.status = status;
    }

    public String getBucket() {
        return bucket;
    }

    public PutBucketAccessMonitorInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public PutBucketAccessMonitorInput setStatus(StatusType status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketAccessMonitorInput{" +
                "bucket='" + bucket + '\'' +
                ", status=" + status +
                '}';
    }

    public static PutBucketAccessMonitorInputBuilder builder() {
        return new PutBucketAccessMonitorInputBuilder();
    }

    public static final class PutBucketAccessMonitorInputBuilder {
        private String bucket;
        private StatusType status;

        private PutBucketAccessMonitorInputBuilder() {
        }

        public PutBucketAccessMonitorInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketAccessMonitorInputBuilder status(StatusType status) {
            this.status = status;
            return this;
        }

        public PutBucketAccessMonitorInput build() {
            PutBucketAccessMonitorInput input = new PutBucketAccessMonitorInput();
            input.bucket = this.bucket;
            input.status = this.status;
            return input;
        }
    }
}