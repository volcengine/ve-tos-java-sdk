package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoutingRuleCondition {
    @JsonProperty("KeyPrefixEquals")
    private String keyPrefixEquals;
    @JsonProperty("HttpErrorCodeReturnedEquals")
    private int httpErrorCodeReturnedEquals;

    public String getKeyPrefixEquals() {
        return keyPrefixEquals;
    }

    public RoutingRuleCondition setKeyPrefixEquals(String keyPrefixEquals) {
        this.keyPrefixEquals = keyPrefixEquals;
        return this;
    }

    public int getHttpErrorCodeReturnedEquals() {
        return httpErrorCodeReturnedEquals;
    }

    public RoutingRuleCondition setHttpErrorCodeReturnedEquals(int httpErrorCodeReturnedEquals) {
        this.httpErrorCodeReturnedEquals = httpErrorCodeReturnedEquals;
        return this;
    }

    @Override
    public String toString() {
        return "RoutingRuleCondition{" +
                "keyPrefixEquals='" + keyPrefixEquals + '\'' +
                ", httpErrorCodeReturnedEquals=" + httpErrorCodeReturnedEquals +
                '}';
    }

    public static RoutingRuleConditionBuilder builder() {
        return new RoutingRuleConditionBuilder();
    }

    public static final class RoutingRuleConditionBuilder {
        private String keyPrefixEquals;
        private int httpErrorCodeReturnedEquals;

        private RoutingRuleConditionBuilder() {
        }

        public RoutingRuleConditionBuilder keyPrefixEquals(String keyPrefixEquals) {
            this.keyPrefixEquals = keyPrefixEquals;
            return this;
        }

        public RoutingRuleConditionBuilder httpErrorCodeReturnedEquals(int httpErrorCodeReturnedEquals) {
            this.httpErrorCodeReturnedEquals = httpErrorCodeReturnedEquals;
            return this;
        }

        public RoutingRuleCondition build() {
            RoutingRuleCondition routingRuleCondition = new RoutingRuleCondition();
            routingRuleCondition.setKeyPrefixEquals(keyPrefixEquals);
            routingRuleCondition.setHttpErrorCodeReturnedEquals(httpErrorCodeReturnedEquals);
            return routingRuleCondition;
        }
    }
}
