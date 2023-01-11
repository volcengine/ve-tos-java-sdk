package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbortInCompleteMultipartUpload {
    @JsonProperty("DaysAfterInitiation")
    private int daysAfterInitiation;

    public int getDaysAfterInitiation() {
        return daysAfterInitiation;
    }

    public AbortInCompleteMultipartUpload setDaysAfterInitiation(int daysAfterInitiation) {
        this.daysAfterInitiation = daysAfterInitiation;
        return this;
    }

    @Override
    public String toString() {
        return "AbortInCompleteMultipartUpload{" +
                "daysAfterInitiation=" + daysAfterInitiation +
                '}';
    }

    public static AbortInCompleteMultipartUploadBuilder builder() {
        return new AbortInCompleteMultipartUploadBuilder();
    }

    public static final class AbortInCompleteMultipartUploadBuilder {
        private int daysAfterInitiation;

        private AbortInCompleteMultipartUploadBuilder() {
        }

        public AbortInCompleteMultipartUploadBuilder daysAfterInitiation(int daysAfterInitiation) {
            this.daysAfterInitiation = daysAfterInitiation;
            return this;
        }

        public AbortInCompleteMultipartUpload build() {
            AbortInCompleteMultipartUpload abortInCompleteMultipartUpload = new AbortInCompleteMultipartUpload();
            abortInCompleteMultipartUpload.setDaysAfterInitiation(daysAfterInitiation);
            return abortInCompleteMultipartUpload;
        }
    }
}
