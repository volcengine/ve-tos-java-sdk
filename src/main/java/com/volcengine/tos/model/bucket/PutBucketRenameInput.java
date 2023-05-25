package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutBucketRenameInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("RenameEnable")
    private boolean renameEnable;

    public String getBucket() {
        return bucket;
    }

    public PutBucketRenameInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public boolean isRenameEnable() {
        return renameEnable;
    }

    public PutBucketRenameInput setRenameEnable(boolean renameEnable) {
        this.renameEnable = renameEnable;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketRenameInput{" +
                "bucket='" + bucket + '\'' +
                ", renameEnable=" + renameEnable +
                '}';
    }

    public static PutBucketRenameInputBuilder builder() {
        return new PutBucketRenameInputBuilder();
    }

    public static final class PutBucketRenameInputBuilder {
        private String bucket;
        private boolean renameEnable;

        private PutBucketRenameInputBuilder() {
        }

        public PutBucketRenameInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketRenameInputBuilder renameEnable(boolean renameEnable) {
            this.renameEnable = renameEnable;
            return this;
        }

        public PutBucketRenameInput build() {
            PutBucketRenameInput putBucketRenameInput = new PutBucketRenameInput();
            putBucketRenameInput.setBucket(bucket);
            putBucketRenameInput.setRenameEnable(renameEnable);
            return putBucketRenameInput;
        }
    }
}
