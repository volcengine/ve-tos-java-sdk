package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

import java.util.Date;

public class SetObjectTimeInput extends GenericInput {
    private String bucket;
    private String key;
    private Date modifyTimestamp;

    public String getBucket() {
        return bucket;
    }

    public SetObjectTimeInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public SetObjectTimeInput setKey(String key) {
        this.key = key;
        return this;
    }

    public Date getModifyTimestamp() {
        return modifyTimestamp;
    }

    public SetObjectTimeInput setModifyTimestamp(Date modifyTimestamp) {
        this.modifyTimestamp = modifyTimestamp;
        return this;
    }

    public static SetObjectTimeInputBuilder builder() {
        return new SetObjectTimeInputBuilder();
    }

    @Override
    public String toString() {
        return "SetObjectTimeInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", modifyTimestamp=" + modifyTimestamp +
                '}';
    }

    public static final class SetObjectTimeInputBuilder {
        private String bucket;
        private String key;
        private Date modifyTimestamp;

        private SetObjectTimeInputBuilder() {
        }

        public SetObjectTimeInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public SetObjectTimeInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SetObjectTimeInputBuilder modifyTimestamp(Date modifyTimestamp) {
            this.modifyTimestamp = modifyTimestamp;
            return this;
        }

        public SetObjectTimeInput build() {
            SetObjectTimeInput setObjectTimeInput = new SetObjectTimeInput();
            setObjectTimeInput.setBucket(bucket);
            setObjectTimeInput.setKey(key);
            setObjectTimeInput.setModifyTimestamp(modifyTimestamp);
            return setObjectTimeInput;
        }
    }
}
