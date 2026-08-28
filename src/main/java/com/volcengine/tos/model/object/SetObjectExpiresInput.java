package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;


public class SetObjectExpiresInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String key;

    @JsonProperty("ObjectExpires")
    private long objectExpires;


    public String getBucket() {
        return bucket;
    }

    public SetObjectExpiresInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public SetObjectExpiresInput setKey(String key) {
        this.key = key;
        return this;
    }

    public long getObjectExpires() {
        return objectExpires;
    }

    public SetObjectExpiresInput setObjectExpires(long objectExpires) {
        this.objectExpires = objectExpires;
        return this;
    }

    public static SetObjectExpiresInputBuilder builder() {
        return new SetObjectExpiresInputBuilder();
    }

    @Override
    public String toString() {
        return "SetObjectExpiresInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", objectExpires=" + objectExpires +
                '}';
    }

    public static final class SetObjectExpiresInputBuilder {
        private String bucket;
        private String key;
        private long objectExpires;

        private SetObjectExpiresInputBuilder() {
        }

        public SetObjectExpiresInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public SetObjectExpiresInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SetObjectExpiresInputBuilder objectExpires(long objectExpires) {
            this.objectExpires = objectExpires;
            return this;
        }

        public SetObjectExpiresInput build() {
            SetObjectExpiresInput setObjectTimeInput = new SetObjectExpiresInput();
            setObjectTimeInput.setBucket(bucket);
            setObjectTimeInput.setKey(key);
            setObjectTimeInput.setObjectExpires(objectExpires);
            return setObjectTimeInput;
        }
    }
}
