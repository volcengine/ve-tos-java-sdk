package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.LifecycleDateSerializer;

import java.util.Date;

public class NoncurrentVersionExpiration {
    @JsonProperty("NoncurrentDays")
    private int noncurrentDays;
    @JsonProperty("NoncurrentDate")
    @JsonSerialize(using = LifecycleDateSerializer.class)
    private Date noncurrentDate;

    public int getNoncurrentDays() {
        return noncurrentDays;
    }

    public NoncurrentVersionExpiration setNoncurrentDays(int noncurrentDays) {
        this.noncurrentDays = noncurrentDays;
        return this;
    }

    public Date getNoncurrentDate() {
        return noncurrentDate;
    }

    public NoncurrentVersionExpiration setNoncurrentDate(Date noncurrentDate) {
        this.noncurrentDate = noncurrentDate;
        return this;
    }

    @Override
    public String toString() {
        return "NoncurrentVersionExpiration{" +
                "noncurrentDays=" + noncurrentDays +
                ", noncurrentDate=" + noncurrentDate +
                '}';
    }

    public static NoncurrentVersionExpirationBuilder builder() {
        return new NoncurrentVersionExpirationBuilder();
    }

    public static final class NoncurrentVersionExpirationBuilder {
        private int noncurrentDays;
        private Date noncurrentDate;

        private NoncurrentVersionExpirationBuilder() {
        }

        public NoncurrentVersionExpirationBuilder noncurrentDays(int noncurrentDays) {
            this.noncurrentDays = noncurrentDays;
            return this;
        }

        public NoncurrentVersionExpirationBuilder noncurrentDate(Date noncurrentDate) {
            this.noncurrentDate = noncurrentDate;
            return this;
        }

        public NoncurrentVersionExpiration build() {
            NoncurrentVersionExpiration noncurrentVersionExpiration = new NoncurrentVersionExpiration();
            noncurrentVersionExpiration.setNoncurrentDays(noncurrentDays);
            noncurrentVersionExpiration.setNoncurrentDate(noncurrentDate);
            return noncurrentVersionExpiration;
        }
    }
}
