package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutBucketObjectSetConfigurationInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("PathLevel")
    private Integer pathLevel;

    @JsonProperty("CustomDelimiter")
    private String customDelimiter;

    @JsonProperty("EnableDefaultObjectSet")
    private Boolean enableDefaultObjectSet;

    @JsonProperty("Qos")
    private ObjectSetQos qos;

    @JsonProperty("StorageQuota")
    private String storageQuota;

    public String getBucket() {
        return bucket;
    }

    public PutBucketObjectSetConfigurationInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public Integer getPathLevel() {
        return pathLevel;
    }

    public PutBucketObjectSetConfigurationInput setPathLevel(Integer pathLevel) {
        this.pathLevel = pathLevel;
        return this;
    }

    public String getCustomDelimiter() {
        return customDelimiter;
    }

    public PutBucketObjectSetConfigurationInput setCustomDelimiter(String customDelimiter) {
        this.customDelimiter = customDelimiter;
        return this;
    }

    public Boolean getEnableDefaultObjectSet() {
        return enableDefaultObjectSet;
    }

    public PutBucketObjectSetConfigurationInput setEnableDefaultObjectSet(Boolean enableDefaultObjectSet) {
        this.enableDefaultObjectSet = enableDefaultObjectSet;
        return this;
    }

    public ObjectSetQos getQos() {
        return qos;
    }

    public PutBucketObjectSetConfigurationInput setQos(ObjectSetQos qos) {
        this.qos = qos;
        return this;
    }

    public String getStorageQuota() {
        return storageQuota;
    }

    public PutBucketObjectSetConfigurationInput setStorageQuota(String storageQuota) {
        this.storageQuota = storageQuota;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketObjectSetConfigurationInput{" +
                "bucket='" + bucket + '\'' +
                ", pathLevel=" + pathLevel +
                ", customDelimiter='" + customDelimiter + '\'' +
                ", enableDefaultObjectSet=" + enableDefaultObjectSet +
                ", qos=" + qos +
                ", storageQuota='" + storageQuota + '\'' +
                '}';
    }

    public static PutBucketObjectSetConfigurationInputBuilder builder() {
        return new PutBucketObjectSetConfigurationInputBuilder();
    }

    public static final class PutBucketObjectSetConfigurationInputBuilder {
        private String bucket;
        private Integer pathLevel;
        private String customDelimiter;
        private Boolean enableDefaultObjectSet;
        private ObjectSetQos qos;
        private String storageQuota;

        private PutBucketObjectSetConfigurationInputBuilder() {
        }

        public PutBucketObjectSetConfigurationInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketObjectSetConfigurationInputBuilder pathLevel(Integer pathLevel) {
            this.pathLevel = pathLevel;
            return this;
        }

        public PutBucketObjectSetConfigurationInputBuilder customDelimiter(String customDelimiter) {
            this.customDelimiter = customDelimiter;
            return this;
        }

        public PutBucketObjectSetConfigurationInputBuilder enableDefaultObjectSet(Boolean enableDefaultObjectSet) {
            this.enableDefaultObjectSet = enableDefaultObjectSet;
            return this;
        }

        public PutBucketObjectSetConfigurationInputBuilder qos(ObjectSetQos qos) {
            this.qos = qos;
            return this;
        }

        public PutBucketObjectSetConfigurationInputBuilder storageQuota(String storageQuota) {
            this.storageQuota = storageQuota;
            return this;
        }

        public PutBucketObjectSetConfigurationInput build() {
            PutBucketObjectSetConfigurationInput input = new PutBucketObjectSetConfigurationInput();
            input.setBucket(bucket);
            input.setPathLevel(pathLevel);
            input.setCustomDelimiter(customDelimiter);
            input.setEnableDefaultObjectSet(enableDefaultObjectSet);
            input.setQos(qos);
            input.setStorageQuota(storageQuota);
            return input;
        }
    }
}
