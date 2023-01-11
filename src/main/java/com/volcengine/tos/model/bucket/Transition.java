package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.model.LifecycleDateSerializer;

import java.util.Date;

public class Transition {
    @JsonProperty("Date")
    @JsonSerialize(using = LifecycleDateSerializer.class)
    private Date date;
    @JsonProperty("Days")
    private int days;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;

    public Date getDate() {
        return date;
    }

    public Transition setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getDays() {
        return days;
    }

    public Transition setDays(int days) {
        this.days = days;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public Transition setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "date=" + date +
                ", days=" + days +
                ", storageClass=" + storageClass +
                '}';
    }

    public static TransitionBuilder builder() {
        return new TransitionBuilder();
    }

    public static final class TransitionBuilder {
        private Date date;
        private int days;
        private StorageClassType storageClass;

        private TransitionBuilder() {
        }

        public TransitionBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public TransitionBuilder days(int days) {
            this.days = days;
            return this;
        }

        public TransitionBuilder storageClass(StorageClassType storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        public Transition build() {
            Transition transition = new Transition();
            transition.setDate(date);
            transition.setDays(days);
            transition.setStorageClass(storageClass);
            return transition;
        }
    }
}
