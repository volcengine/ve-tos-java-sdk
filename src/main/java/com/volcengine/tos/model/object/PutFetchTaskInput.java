package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class PutFetchTaskInput extends GenericInput {
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
    private String contentMD5;
    @JsonProperty("CallbackUrl")
    private String callbackUrl;
    @JsonProperty("CallbackHost")
    private String callbackHost;
    @JsonProperty("CallbackBodyType")
    private String callbackBodyType;
    @JsonProperty("CallbackBody")
    private String callbackBody;

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

    @JsonIgnore
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

    @Deprecated
    @JsonIgnore
    public String getHexMD5() {
        return "";
    }

    @Deprecated
    public PutFetchTaskInput setHexMD5(String hexMD5) {
        return this;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public PutFetchTaskInput setContentMD5(String contentMD5) {
        this.contentMD5 = this.contentMD5;
        return this;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public PutFetchTaskInput setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public String getCallbackHost() {
        return callbackHost;
    }

    public PutFetchTaskInput setCallbackHost(String callbackHost) {
        this.callbackHost = callbackHost;
        return this;
    }

    public String getCallbackBodyType() {
        return callbackBodyType;
    }

    public PutFetchTaskInput setCallbackBodyType(String callbackBodyType) {
        this.callbackBodyType = callbackBodyType;
        return this;
    }

    public String getCallbackBody() {
        return callbackBody;
    }

    public void setCallbackBody(String callbackBody) {
        this.callbackBody = callbackBody;
    }

    @Override
    public String toString() {
        return "PutFetchTaskInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", options=" + options +
                ", url='" + url + '\'' +
                ", ignoreSameKey=" + ignoreSameKey +
                ", contentMD5='" + contentMD5 + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", callbackHost='" + callbackHost + '\'' +
                ", callbackBodyType='" + callbackBodyType + '\'' +
                ", callbackBody='" + callbackBody + '\'' +
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
        private String contentMD5;
        private String callbackUrl;
        private String callbackHost;
        private String callbackBodyType;
        private String callbackBody;

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

        @Deprecated
        public PutFetchTaskInputBuilder hexMD5(String hexMD5) {
            return this;
        }
        public PutFetchTaskInputBuilder contentMD5(String contentMD5) {
            this.contentMD5 = contentMD5;
            return this;
        }

        public PutFetchTaskInputBuilder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public PutFetchTaskInputBuilder callbackHost(String callbackHost) {
            this.callbackHost = callbackHost;
            return this;
        }

        public PutFetchTaskInputBuilder callbackBodyType(String callbackBodyType) {
            this.callbackBodyType = callbackBodyType;
            return this;
        }

        public PutFetchTaskInputBuilder callbackBody(String callbackBody) {
            this.callbackBody = callbackBody;
            return this;
        }

        public PutFetchTaskInput build() {
            PutFetchTaskInput putFetchTaskInput = new PutFetchTaskInput();
            putFetchTaskInput.setBucket(bucket);
            putFetchTaskInput.setKey(key);
            putFetchTaskInput.setOptions(options);
            putFetchTaskInput.setUrl(url);
            putFetchTaskInput.setIgnoreSameKey(ignoreSameKey);
            putFetchTaskInput.setContentMD5(contentMD5);
            putFetchTaskInput.setCallbackUrl(callbackUrl);
            putFetchTaskInput.setCallbackHost(callbackHost);
            putFetchTaskInput.setCallbackBody(callbackBody);
            putFetchTaskInput.setCallbackBodyType(callbackBodyType);
            return putFetchTaskInput;
        }
    }
}
