package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class DoesObjectExistInput extends GenericInput {
    private String bucket;
    private String key;
    private String VersionID;
    private boolean isOnlyInTOS;

    public DoesObjectExistInput(){
    }

    public String getBucket() {
        return bucket;
    }

    public DoesObjectExistInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DoesObjectExistInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return VersionID;
    }

    public DoesObjectExistInput setVersionID(String versionID) {
        this.VersionID = versionID;
        return this;
    }

    public boolean isOnlyInTOS() {
        return isOnlyInTOS;
    }

    public DoesObjectExistInput setOnlyInTOS(boolean isOnlyInTOS) {
        this.isOnlyInTOS = isOnlyInTOS;
        return this;
    }

    @Override
    public String toString() {
        return "DoesObjectExistInput{"
                + "bucket='" + bucket + '\''
                + ", key='" + key + '\''
                + ", VersionID='" + VersionID + '\''
                + ", IsOnlyInTOS=" + isOnlyInTOS +
                '}';
    }

    public static DoesObjectExistInputBuilder builder() {
        return new DoesObjectExistInputBuilder();
    }

    public static final class DoesObjectExistInputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private boolean isOnlyInTOS;

        private DoesObjectExistInputBuilder() {
        }

        public DoesObjectExistInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DoesObjectExistInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public DoesObjectExistInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public DoesObjectExistInputBuilder isOnlyInTOS(boolean isOnlyInTOS) {
            this.isOnlyInTOS = isOnlyInTOS;
            return this;
        }

        public DoesObjectExistInput build() {
            DoesObjectExistInput doesObjectExistInput = new DoesObjectExistInput();
            doesObjectExistInput.setBucket(this.bucket);
            doesObjectExistInput.setKey(this.key);
            doesObjectExistInput.setVersionID(this.versionID);
            doesObjectExistInput.setOnlyInTOS(this.isOnlyInTOS);
            return doesObjectExistInput;
        }
    }
}