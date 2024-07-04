package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DeleteBucketCustomDomainInput extends GenericInput {
    private String bucket;
    private String domain;

    public String getBucket() {
        return bucket;
    }

    public DeleteBucketCustomDomainInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public DeleteBucketCustomDomainInput setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteBucketCustomDomainInput{" +
                "bucket='" + bucket + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }

    public static DeleteBucketCustomDomainInputBuilder builder() {
        return new DeleteBucketCustomDomainInputBuilder();
    }

    public static final class DeleteBucketCustomDomainInputBuilder {
        private String bucket;
        private String domain;

        private DeleteBucketCustomDomainInputBuilder() {
        }

        public DeleteBucketCustomDomainInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteBucketCustomDomainInputBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public DeleteBucketCustomDomainInput build() {
            DeleteBucketCustomDomainInput deleteBucketCustomDomainInput = new DeleteBucketCustomDomainInput();
            deleteBucketCustomDomainInput.setBucket(bucket);
            deleteBucketCustomDomainInput.setDomain(domain);
            return deleteBucketCustomDomainInput;
        }
    }
}
