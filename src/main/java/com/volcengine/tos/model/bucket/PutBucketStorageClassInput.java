package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.StorageClassType;

public class PutBucketStorageClassInput {
    private String bucket;
    private StorageClassType storageClass;

    public String getBucket() {
        return bucket;
    }

    public PutBucketStorageClassInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public PutBucketStorageClassInput setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketStorageClassInput{" +
                "bucket='" + bucket + '\'' +
                ", storageClass=" + storageClass +
                '}';
    }

    public static PutBucketStorageClassInputBuilder builder() {
        return new PutBucketStorageClassInputBuilder();
    }

    public static final class PutBucketStorageClassInputBuilder {
        private String bucket;
        private StorageClassType storageClass;

        private PutBucketStorageClassInputBuilder() {
        }

        public PutBucketStorageClassInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketStorageClassInputBuilder storageClass(StorageClassType storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        public PutBucketStorageClassInput build() {
            PutBucketStorageClassInput putBucketStorageClassInput = new PutBucketStorageClassInput();
            putBucketStorageClassInput.setBucket(bucket);
            putBucketStorageClassInput.setStorageClass(storageClass);
            return putBucketStorageClassInput;
        }
    }
}
