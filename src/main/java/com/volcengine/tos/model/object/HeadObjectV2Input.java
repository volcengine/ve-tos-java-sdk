package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class HeadObjectV2Input extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;

    private ObjectMetaRequestOptions options;

    public HeadObjectV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public HeadObjectV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public HeadObjectV2Input setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public HeadObjectV2Input setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

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

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    @Override
    public String toString() {
        return "HeadObjectV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", options=" + options +
                '}';
    }

    public static HeadObjectInputV2Builder builder() {
        return new HeadObjectInputV2Builder();
    }

    public static final class HeadObjectInputV2Builder {
        private String bucket;
        private String key;
        private String versionID;
        private ObjectMetaRequestOptions options;

        private HeadObjectInputV2Builder() {
        }

        public HeadObjectInputV2Builder buckets(String buckets) {
            this.bucket = buckets;
            return this;
        }

        public HeadObjectInputV2Builder key(String key) {
            this.key = key;
            return this;
        }

        public HeadObjectInputV2Builder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public HeadObjectInputV2Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public HeadObjectInputV2Builder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public HeadObjectV2Input build() {
            HeadObjectV2Input headObjectInputV2 = new HeadObjectV2Input();
            headObjectInputV2.bucket = this.bucket;
            headObjectInputV2.key = this.key;
            headObjectInputV2.versionID = this.versionID;
            headObjectInputV2.options = this.options;
            return headObjectInputV2;
        }
    }
}
