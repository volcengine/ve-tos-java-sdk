package com.volcengine.tos.model.object;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.PostSignatureConditionSerializer;

@JsonSerialize(using = PostSignatureConditionSerializer.class)
public class PostSignatureCondition {
    private String key;
    private String value;
    private String operator;

    public PostSignatureCondition() {
    }

    public PostSignatureCondition(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public PostSignatureCondition(String key, String value, String operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public PostSignatureCondition setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PostSignatureCondition setValue(String value) {
        this.value = value;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PostSignatureCondition setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public String toString() {
        return "PostSignatureCondition{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    public static PostSignatureConditionBuilder builder() {
        return new PostSignatureConditionBuilder();
    }

    public static final class PostSignatureConditionBuilder {
        private String key;
        private String value;
        private String operator;

        private PostSignatureConditionBuilder() {
        }

        public PostSignatureConditionBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PostSignatureConditionBuilder value(String value) {
            this.value = value;
            return this;
        }

        public PostSignatureConditionBuilder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public PostSignatureCondition build() {
            PostSignatureCondition postSignatureCondition = new PostSignatureCondition();
            postSignatureCondition.setKey(key);
            postSignatureCondition.setValue(value);
            postSignatureCondition.setOperator(operator);
            return postSignatureCondition;
        }
    }
}
