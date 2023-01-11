package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoncurrentVersionExpiration {
    @JsonProperty("NoncurrentDays")
    private int noncurrentDays;

    public int getNoncurrentDays() {
        return noncurrentDays;
    }

    public NoncurrentVersionExpiration setNoncurrentDays(int noncurrentDays) {
        this.noncurrentDays = noncurrentDays;
        return this;
    }

    @Override
    public String toString() {
        return "NoncurrentVersionExpiration{" +
                "noncurrentDays=" + noncurrentDays +
                '}';
    }

    public static NoncurrentVersionExpirationBuilder builder() {
        return new NoncurrentVersionExpirationBuilder();
    }

    public static final class NoncurrentVersionExpirationBuilder {
        private int noncurrentDays;

        private NoncurrentVersionExpirationBuilder() {
        }

        public NoncurrentVersionExpirationBuilder noncurrentDays(int noncurrentDays) {
            this.noncurrentDays = noncurrentDays;
            return this;
        }

        public NoncurrentVersionExpiration build() {
            NoncurrentVersionExpiration noncurrentVersionExpiration = new NoncurrentVersionExpiration();
            noncurrentVersionExpiration.setNoncurrentDays(noncurrentDays);
            return noncurrentVersionExpiration;
        }
    }
}
