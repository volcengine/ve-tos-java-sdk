package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.LifecycleDateSerializer;

import java.util.Date;

public class Expiration {
    @JsonProperty("Date")

    @JsonSerialize(using = LifecycleDateSerializer.class)
    private Date date;
    @JsonProperty("Days")
    private int days;

    public Date getDate() {
        return date;
    }

    public Expiration setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getDays() {
        return days;
    }

    public Expiration setDays(int days) {
        this.days = days;
        return this;
    }

    @Override
    public String toString() {
        return "Expiration{" +
                "date=" + date +
                ", days=" + days +
                '}';
    }

    public static ExpirationBuilder builder() {
        return new ExpirationBuilder();
    }

    public static final class ExpirationBuilder {
        private Date date;
        private int days;

        private ExpirationBuilder() {
        }

        public ExpirationBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public ExpirationBuilder days(int days) {
            this.days = days;
            return this;
        }

        public Expiration build() {
            Expiration expiration = new Expiration();
            expiration.setDate(date);
            expiration.setDays(days);
            return expiration;
        }
    }
}
