package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BucketEncryptionRule {

    @JsonProperty("ApplyServerSideEncryptionByDefault")
    private ApplyServerSideEncryptionByDefault applyServerSideEncryptionByDefault;

    public ApplyServerSideEncryptionByDefault getApplyServerSideEncryptionByDefault() {
        return applyServerSideEncryptionByDefault;
    }

    public BucketEncryptionRule setApplyServerSideEncryptionByDefault(ApplyServerSideEncryptionByDefault applyServerSideEncryptionByDefault) {
        this.applyServerSideEncryptionByDefault = applyServerSideEncryptionByDefault;
        return this;
    }

    @Override
    public String toString() {
        return "BucketEncryptionRule{" +
                "applyServerSideEncryptionByDefault=" + applyServerSideEncryptionByDefault +
                '}';
    }

    public static class ApplyServerSideEncryptionByDefault{
        @JsonProperty("SSEAlgorithm")
        private String sseAlgorithm;
        @JsonProperty("KMSMasterKeyID")
        private String kmsMasterKeyID;

        public String getSseAlgorithm() {
            return sseAlgorithm;
        }

        public ApplyServerSideEncryptionByDefault setSseAlgorithm(String sseAlgorithm) {
            this.sseAlgorithm = sseAlgorithm;
            return this;
        }

        public String getKmsMasterKeyID() {
            return kmsMasterKeyID;
        }

        public ApplyServerSideEncryptionByDefault setKmsMasterKeyID(String kmsMasterKeyID) {
            this.kmsMasterKeyID = kmsMasterKeyID;
            return this;
        }

        @Override
        public String toString() {
            return "ApplyServerSideEncryptionByDefault{" +
                    "sseAlgorithm='" + sseAlgorithm + '\'' +
                    ", kmsMasterKeyID='" + kmsMasterKeyID + '\'' +
                    '}';
        }
    }
}
