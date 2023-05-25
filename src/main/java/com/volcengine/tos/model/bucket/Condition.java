package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Condition {
    @JsonProperty("HttpCode")
    private int httpCode;
    @JsonProperty("KeyPrefix")
    private String keyPrefix;
    @JsonProperty("KeySuffix")
    private String keySuffix;

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

    @Override
    public String toString() {
        return "Condition{" +
                "httpCode=" + httpCode +
                ", keyPrefix='" + keyPrefix + '\'' +
                ", keySuffix='" + keySuffix + '\'' +
                '}';
    }

    public static ConditionBuilder builder() {
        return new ConditionBuilder();
    }

    public static final class ConditionBuilder {
        private int httpCode;
        private String keyPrefix;
        private String keySuffix;

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

        public Condition build() {
            Condition condition = new Condition();
            condition.setHttpCode(httpCode);
            condition.setKeyPrefix(keyPrefix);
            condition.setKeySuffix(keySuffix);
            return condition;
        }
    }
}
