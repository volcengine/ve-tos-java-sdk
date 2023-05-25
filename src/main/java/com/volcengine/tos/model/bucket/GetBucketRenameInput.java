package com.volcengine.tos.model.bucket;

public class GetBucketRenameInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketRenameInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketRenameInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketRenameInputBuilder builder() {
        return new GetBucketRenameInputBuilder();
    }

    public static final class GetBucketRenameInputBuilder {
        private String bucket;

        private GetBucketRenameInputBuilder() {
        }

        public GetBucketRenameInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketRenameInput build() {
            GetBucketRenameInput getBucketRenameInput = new GetBucketRenameInput();
            getBucketRenameInput.setBucket(bucket);
            return getBucketRenameInput;
        }
    }
}
