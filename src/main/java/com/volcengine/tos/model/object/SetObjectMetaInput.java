package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class SetObjectMetaInput extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;
    private long objectExpires = -1;

    private ObjectMetaRequestOptions options;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getVersionID() {
        return versionID;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public SetObjectMetaInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public SetObjectMetaInput setKey(String key) {
        this.key = key;
        return this;
    }

    public SetObjectMetaInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public SetObjectMetaInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public long getObjectExpires() {
        return objectExpires;
    }

    public SetObjectMetaInput setObjectExpires(long objectExpires) {
        this.objectExpires = objectExpires;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    @Override
    public String toString() {
        return "SetObjectMetaInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", objectExpires=" + objectExpires +
                ", options=" + options +
                '}';
    }

    public static SetObjectMetaInputBuilder builder() {
        return new SetObjectMetaInputBuilder();
    }

    public static final class SetObjectMetaInputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private ObjectMetaRequestOptions options;
        private long objectExpires = -1;

        private SetObjectMetaInputBuilder() {
        }

        public SetObjectMetaInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public SetObjectMetaInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SetObjectMetaInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public SetObjectMetaInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public SetObjectMetaInputBuilder objectExpires(long objectExpires) {
            this.objectExpires = objectExpires;
            return this;
        }

        public SetObjectMetaInput build() {
            SetObjectMetaInput setObjectMetaInput = new SetObjectMetaInput();
            setObjectMetaInput.bucket = this.bucket;
            setObjectMetaInput.key = this.key;
            setObjectMetaInput.versionID = this.versionID;
            setObjectMetaInput.options = this.options;
            setObjectMetaInput.objectExpires = this.objectExpires;
            return setObjectMetaInput;
        }
    }
}
