package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Condition {
    @JsonProperty("HttpCode")
    private int httpCode;

    public int getHttpCode() {
        return httpCode;
    }

    public Condition setHttpCode(int httpCode) {
        this.httpCode = httpCode;
        return this;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "httpCode=" + httpCode +
                '}';
    }

    public static ConditionBuilder builder() {
        return new ConditionBuilder();
    }

    public static final class ConditionBuilder {
        private int httpCode;

        private ConditionBuilder() {
        }

        public ConditionBuilder httpCode(int httpCode) {
            this.httpCode = httpCode;
            return this;
        }

        public Condition build() {
            Condition condition = new Condition();
            condition.setHttpCode(httpCode);
            return condition;
        }
    }
}
