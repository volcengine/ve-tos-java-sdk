package com.volcengine.tos.model.object;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.PolicySignatureConditionSerializer;

@JsonSerialize(using = PolicySignatureConditionSerializer.class)
public class PolicySignatureCondition {
    private String key;
    private String value;
    private String operator;

    public String getKey() {
        return key;
    }

    public PolicySignatureCondition setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PolicySignatureCondition setValue(String value) {
        this.value = value;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PolicySignatureCondition setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public String toString() {
        return "PolicySignatureCondition{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    public static PolicySignatureConditionBuilder builder() {
        return new PolicySignatureConditionBuilder();
    }

    public static final class PolicySignatureConditionBuilder {
        private String key;
        private String value;
        private String operator;

        private PolicySignatureConditionBuilder() {
        }

        public PolicySignatureConditionBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PolicySignatureConditionBuilder value(String value) {
            this.value = value;
            return this;
        }

        public PolicySignatureConditionBuilder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public PolicySignatureCondition build() {
            PolicySignatureCondition policySignatureCondition = new PolicySignatureCondition();
            policySignatureCondition.setKey(key);
            policySignatureCondition.setValue(value);
            policySignatureCondition.setOperator(operator);
            return policySignatureCondition;
        }
    }
}
