package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.model.LifecycleDateSerializer;

import java.util.Date;

public class NoncurrentVersionTransition {
    @JsonProperty("NoncurrentDays")
    private int noncurrentDays;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("NoncurrentDate")
    @JsonSerialize(using = LifecycleDateSerializer.class)
    private Date noncurrentDate;

    public int getNoncurrentDays() {
        return noncurrentDays;
    }

    public NoncurrentVersionTransition setNoncurrentDays(int noncurrentDays) {
        this.noncurrentDays = noncurrentDays;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public NoncurrentVersionTransition setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public Date getNoncurrentDate() {
        return noncurrentDate;
    }

    public NoncurrentVersionTransition setNoncurrentDate(Date noncurrentDate) {
        this.noncurrentDate = noncurrentDate;
        return this;
    }

    @Override
    public String toString() {
        return "NoncurrentVersionTransition{" +
                "noncurrentDays=" + noncurrentDays +
                ", storageClass=" + storageClass +
                ", noncurrentDate=" + noncurrentDate +
                '}';
    }

    public static NoncurrentVersionTransitionBuilder builder() {
        return new NoncurrentVersionTransitionBuilder();
    }

    public static final class NoncurrentVersionTransitionBuilder {
        private int noncurrentDays;
        private StorageClassType storageClass;
        private Date noncurrentDate;

        private NoncurrentVersionTransitionBuilder() {
        }

        public NoncurrentVersionTransitionBuilder noncurrentDays(int noncurrentDays) {
            this.noncurrentDays = noncurrentDays;
            return this;
        }

        public NoncurrentVersionTransitionBuilder storageClass(StorageClassType storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        public NoncurrentVersionTransitionBuilder noncurrentDate(Date noncurrentDate) {
            this.noncurrentDate = noncurrentDate;
            return this;
        }

        public NoncurrentVersionTransition build() {
            NoncurrentVersionTransition noncurrentVersionTransition = new NoncurrentVersionTransition();
            noncurrentVersionTransition.setNoncurrentDays(noncurrentDays);
            noncurrentVersionTransition.setStorageClass(storageClass);
            noncurrentVersionTransition.setNoncurrentDate(noncurrentDate);
            return noncurrentVersionTransition;
        }
    }
}
