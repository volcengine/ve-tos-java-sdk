package com.volcengine.tos.model.object;

import java.util.Map;

public class PreSignedURLInput {
    private String httpMethod;
    private String bucket;
    private String key;
    /**
     * 过期时间，单位：秒，默认 3600 秒，最大 7 天，取值范围 [1, 604800]
     */
    private long expires;
    private Map<String, String> header;
    private Map<String, String> query;

    /**
     * 如果该参数不为空，则生成的 signed url 使用该参数作为域名，而不是使用 TOS Client 初始化参数中的 endpoint
     */
    private String alternativeEndpoint;

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public long getExpires() {
        return expires;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public String getAlternativeEndpoint() {
        return alternativeEndpoint;
    }

    public PreSignedURLInput setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public PreSignedURLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public PreSignedURLInput setKey(String key) {
        this.key = key;
        return this;
    }

    public PreSignedURLInput setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public PreSignedURLInput setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public PreSignedURLInput setQuery(Map<String, String> query) {
        this.query = query;
        return this;
    }

    public PreSignedURLInput setAlternativeEndpoint(String alternativeEndpoint) {
        this.alternativeEndpoint = alternativeEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedURLInput{" +
                "httpMethod=" + httpMethod +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", expires=" + expires +
                ", header=" + header +
                ", query=" + query +
                ", alternativeEndpoint='" + alternativeEndpoint + '\'' +
                '}';
    }

    public static PreSignedURLInputBuilder builder() {
        return new PreSignedURLInputBuilder();
    }

    public static final class PreSignedURLInputBuilder {
        private String httpMethod;
        private String bucket;
        private String key;
        private long expires;
        private Map<String, String> header;
        private Map<String, String> query;
        private String alternativeEndpoint;

        private PreSignedURLInputBuilder() {
        }

        public PreSignedURLInputBuilder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public PreSignedURLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PreSignedURLInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PreSignedURLInputBuilder expires(long expires) {
            this.expires = expires;
            return this;
        }

        public PreSignedURLInputBuilder header(Map<String, String> header) {
            this.header = header;
            return this;
        }

        public PreSignedURLInputBuilder query(Map<String, String> query) {
            this.query = query;
            return this;
        }

        public PreSignedURLInputBuilder alternativeEndpoint(String alternativeEndpoint) {
            this.alternativeEndpoint = alternativeEndpoint;
            return this;
        }

        public PreSignedURLInput build() {
            PreSignedURLInput preSignedURLInput = new PreSignedURLInput();
            preSignedURLInput.expires = this.expires;
            preSignedURLInput.query = this.query;
            preSignedURLInput.alternativeEndpoint = this.alternativeEndpoint;
            preSignedURLInput.bucket = this.bucket;
            preSignedURLInput.header = this.header;
            preSignedURLInput.httpMethod = this.httpMethod;
            preSignedURLInput.key = this.key;
            return preSignedURLInput;
        }
    }
}
