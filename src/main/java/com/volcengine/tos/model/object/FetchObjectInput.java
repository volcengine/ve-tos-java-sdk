package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class FetchObjectInput {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String key;
    @JsonIgnore
    private ObjectMetaRequestOptions options;
    @JsonProperty("URL")
    private String url;
    @JsonProperty("IgnoreSameKey")
    private boolean ignoreSameKey;
    @JsonProperty("ContentMD5")
    private String contentMD5;

    public String getBucket() {
        return bucket;
    }

    public FetchObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public FetchObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public FetchObjectInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FetchObjectInput setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isIgnoreSameKey() {
        return ignoreSameKey;
    }

    public FetchObjectInput setIgnoreSameKey(boolean ignoreSameKey) {
        this.ignoreSameKey = ignoreSameKey;
        return this;
    }

    @Deprecated
    public String getHexMD5() {
        return "";
    }

    @Deprecated
    public FetchObjectInput setHexMD5(String hexMD5) {
        return this;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public FetchObjectInput setContentMD5(String contentMD5) {
        this.contentMD5 = this.contentMD5;
        return this;
    }

    @Override
    public String toString() {
        return "FetchObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", options=" + options +
                ", url='" + url + '\'' +
                ", ignoreSameKey=" + ignoreSameKey +
                ", contentMd5='" + contentMD5 + '\'' +
                '}';
    }

    public static FetchObjectInputBuilder builder() {
        return new FetchObjectInputBuilder();
    }

    public static final class FetchObjectInputBuilder {
        private String bucket;
        private String key;
        private ObjectMetaRequestOptions options;
        private String url;
        private boolean ignoreSameKey;
        private String contentMD5;

        private FetchObjectInputBuilder() {
        }

        public FetchObjectInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public FetchObjectInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public FetchObjectInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public FetchObjectInputBuilder url(String url) {
            this.url = url;
            return this;
        }

        public FetchObjectInputBuilder ignoreSameKey(boolean ignoreSameKey) {
            this.ignoreSameKey = ignoreSameKey;
            return this;
        }

        @Deprecated
        public FetchObjectInputBuilder hexMD5(String hexMD5) {
            return this;
        }

        public FetchObjectInputBuilder contentMD5(String contentMD5) {
            this.contentMD5 = contentMD5;
            return this;
        }

        public FetchObjectInput build() {
            FetchObjectInput fetchObjectInput = new FetchObjectInput();
            fetchObjectInput.setBucket(bucket);
            fetchObjectInput.setKey(key);
            fetchObjectInput.setOptions(options);
            fetchObjectInput.setUrl(url);
            fetchObjectInput.setIgnoreSameKey(ignoreSameKey);
            fetchObjectInput.setContentMD5(contentMD5);
            return fetchObjectInput;
        }
    }
}
