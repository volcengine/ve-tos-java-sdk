package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteBucketWebsiteInput extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketWebsiteInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketWebsiteInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static DeleteBucketWebsiteInputBuilder builder() {
        return new DeleteBucketWebsiteInputBuilder();
    }

    public static final class DeleteBucketWebsiteInputBuilder {
        private String bucket;

        private DeleteBucketWebsiteInputBuilder() {
        }

        public DeleteBucketWebsiteInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketWebsiteInput build() {
            DeleteBucketWebsiteInput deleteBucketWebsiteInput = new DeleteBucketWebsiteInput();
            deleteBucketWebsiteInput.setBucket(bucket);
            return deleteBucketWebsiteInput;
        }
    }
}
