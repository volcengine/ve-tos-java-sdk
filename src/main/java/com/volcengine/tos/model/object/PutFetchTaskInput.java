package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PutFetchTaskInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Object")
    private String key;
    @JsonIgnore
    private ObjectMetaRequestOptions options;
    @JsonProperty("URL")
    private String url;
    @JsonProperty("IgnoreSameKey")
    private boolean ignoreSameKey;
    @JsonProperty("ContentMD5")
    private String hexMD5;

    public String getBucket() {
        return bucket;
    }

    public PutFetchTaskInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PutFetchTaskInput setKey(String key) {
        this.key = key;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public PutFetchTaskInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PutFetchTaskInput setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isIgnoreSameKey() {
        return ignoreSameKey;
    }

    public PutFetchTaskInput setIgnoreSameKey(boolean ignoreSameKey) {
        this.ignoreSameKey = ignoreSameKey;
        return this;
    }

    public String getHexMD5() {
        return hexMD5;
    }

    public PutFetchTaskInput setHexMD5(String hexMD5) {
        this.hexMD5 = hexMD5;
        return this;
    }

    @Override
    public String toString() {
        return "PutFetchTaskInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", options=" + options +
                ", url='" + url + '\'' +
                ", ignoreSameKey=" + ignoreSameKey +
                ", hexMD5='" + hexMD5 + '\'' +
                '}';
    }

    public static PutFetchTaskInputBuilder builder() {
        return new PutFetchTaskInputBuilder();
    }

    public static final class PutFetchTaskInputBuilder {
        private String bucket;
        private String key;
        private ObjectMetaRequestOptions options;
        private String url;
        private boolean ignoreSameKey;
        private String hexMD5;

        private PutFetchTaskInputBuilder() {
        }

        public PutFetchTaskInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutFetchTaskInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PutFetchTaskInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public PutFetchTaskInputBuilder url(String url) {
            this.url = url;
            return this;
        }

        public PutFetchTaskInputBuilder ignoreSameKey(boolean ignoreSameKey) {
            this.ignoreSameKey = ignoreSameKey;
            return this;
        }

        public PutFetchTaskInputBuilder hexMD5(String hexMD5) {
            this.hexMD5 = hexMD5;
            return this;
        }

        public PutFetchTaskInput build() {
            PutFetchTaskInput putFetchTaskInput = new PutFetchTaskInput();
            putFetchTaskInput.setBucket(bucket);
            putFetchTaskInput.setKey(key);
            putFetchTaskInput.setOptions(options);
            putFetchTaskInput.setUrl(url);
            putFetchTaskInput.setIgnoreSameKey(ignoreSameKey);
            putFetchTaskInput.setHexMD5(hexMD5);
            return putFetchTaskInput;
        }
    }
}
