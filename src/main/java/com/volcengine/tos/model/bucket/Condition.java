package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Condition {
    @JsonProperty("HttpCode")
    private int httpCode;
    @JsonProperty("KeyPrefix")
    private String keyPrefix;
    @JsonProperty("KeySuffix")
    private String keySuffix;
    @JsonProperty("AllowHost")
    private List<String> allowHost;
    @JsonProperty("HttpMethod")
    private List<String> httpMethod;

    public int getHttpCode() {
        return httpCode;
    }

    public Condition setHttpCode(int httpCode) {
        this.httpCode = httpCode;
        return this;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public Condition setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
        return this;
    }

    public String getKeySuffix() {
        return keySuffix;
    }

    public Condition setKeySuffix(String keySuffix) {
        this.keySuffix = keySuffix;
        return this;
    }

    public List<String> getAllowHost() {
        return allowHost;
    }

    public Condition setAllowHost(List<String> allowHost) {
        this.allowHost = allowHost;
        return this;
    }

    public List<String> getHttpMethod() {
        return httpMethod;
    }

    public Condition setHttpMethod(List<String> httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "httpCode=" + httpCode +
                ", keyPrefix='" + keyPrefix + '\'' +
                ", keySuffix='" + keySuffix + '\'' +
                ", allowHost=" + allowHost + '\'' +
                ", httpMethod=" + httpMethod + '\'' +
                '}';
    }

    public static ConditionBuilder builder() {
        return new ConditionBuilder();
    }

    public static final class ConditionBuilder {
        private int httpCode;
        private String keyPrefix;
        private String keySuffix;
        private List<String> allowHost;
        private List<String> httpMethod;

        private ConditionBuilder() {
        }

        public ConditionBuilder httpCode(int httpCode) {
            this.httpCode = httpCode;
            return this;
        }

        public ConditionBuilder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        public ConditionBuilder keySuffix(String keySuffix) {
            this.keySuffix = keySuffix;
            return this;
        }

        public ConditionBuilder allowHost(List<String> allowHost) {
            this.allowHost = allowHost;
            return this;
        }

        public ConditionBuilder httpMethod(List<String> httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Condition build() {
            Condition condition = new Condition();
            condition.setHttpCode(httpCode);
            condition.setKeyPrefix(keyPrefix);
            condition.setKeySuffix(keySuffix);
            condition.setAllowHost(allowHost);
            condition.setHttpMethod(httpMethod);
            return condition;
        }
    }
}
