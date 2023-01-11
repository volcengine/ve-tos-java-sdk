package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassType;

public class NoncurrentVersionTransition {
    @JsonProperty("NoncurrentDays")
    private int noncurrentDays;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;

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

    @Override
    public String toString() {
        return "NoncurrentVersionTransition{" +
                "noncurrentDays=" + noncurrentDays +
                ", storageClass=" + storageClass +
                '}';
    }

    public static NoncurrentVersionTransitionBuilder builder() {
        return new NoncurrentVersionTransitionBuilder();
    }

    public static final class NoncurrentVersionTransitionBuilder {
        private int noncurrentDays;
        private StorageClassType storageClass;

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

        public NoncurrentVersionTransition build() {
            NoncurrentVersionTransition noncurrentVersionTransition = new NoncurrentVersionTransition();
            noncurrentVersionTransition.setNoncurrentDays(noncurrentDays);
            noncurrentVersionTransition.setStorageClass(storageClass);
            return noncurrentVersionTransition;
        }
    }
}
