package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class ListBucketCustomDomainInput extends GenericInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public ListBucketCustomDomainInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "ListBucketCustomDomainInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static ListBucketCustomDomainInputBuilder builder() {
        return new ListBucketCustomDomainInputBuilder();
    }

    public static final class ListBucketCustomDomainInputBuilder {
        private String bucket;

        private ListBucketCustomDomainInputBuilder() {
        }

        public ListBucketCustomDomainInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListBucketCustomDomainInput build() {
            ListBucketCustomDomainInput listBucketCustomDomainInput = new ListBucketCustomDomainInput();
            listBucketCustomDomainInput.setBucket(bucket);
            return listBucketCustomDomainInput;
        }
    }
}
