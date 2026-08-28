package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutObjectSetQuotaInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String objectSetName;

    @JsonProperty("StorageQuota")
    private String storageQuota;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetQuotaInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public PutObjectSetQuotaInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public String getStorageQuota() {
        return storageQuota;
    }

    public PutObjectSetQuotaInput setStorageQuota(String storageQuota) {
        this.storageQuota = storageQuota;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetQuotaInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                ", storageQuota='" + storageQuota + '\'' +
                '}';
    }

    public static PutObjectSetQuotaInputBuilder builder() {
        return new PutObjectSetQuotaInputBuilder();
    }

    public static final class PutObjectSetQuotaInputBuilder {
        private String bucket;
        private String objectSetName;
        private String storageQuota;

        private PutObjectSetQuotaInputBuilder() {
        }

        public PutObjectSetQuotaInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetQuotaInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public PutObjectSetQuotaInputBuilder storageQuota(String storageQuota) {
            this.storageQuota = storageQuota;
            return this;
        }

        public PutObjectSetQuotaInput build() {
            PutObjectSetQuotaInput input = new PutObjectSetQuotaInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            input.setStorageQuota(storageQuota);
            return input;
        }
    }
}
