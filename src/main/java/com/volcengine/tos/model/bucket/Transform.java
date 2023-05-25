package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transform {
    @JsonProperty("WithKeyPrefix")
    private String withKeyPrefix;
    @JsonProperty("WithKeySuffix")
    private String withKeySuffix;
    @JsonProperty("ReplaceKeyPrefix")
    private ReplaceKeyPrefix replaceKeyPrefix;

    public String getWithKeyPrefix() {
        return withKeyPrefix;
    }

    public Transform setWithKeyPrefix(String withKeyPrefix) {
        this.withKeyPrefix = withKeyPrefix;
        return this;
    }

    public String getWithKeySuffix() {
        return withKeySuffix;
    }

    public Transform setWithKeySuffix(String withKeySuffix) {
        this.withKeySuffix = withKeySuffix;
        return this;
    }

    public ReplaceKeyPrefix getReplaceKeyPrefix() {
        return replaceKeyPrefix;
    }

    public Transform setReplaceKeyPrefix(ReplaceKeyPrefix replaceKeyPrefix) {
        this.replaceKeyPrefix = replaceKeyPrefix;
        return this;
    }

    @Override
    public String toString() {
        return "Transform{" +
                "withKeyPrefix='" + withKeyPrefix + '\'' +
                ", withKeySuffix='" + withKeySuffix + '\'' +
                ", replaceKeyPrefix=" + replaceKeyPrefix +
                '}';
    }

    public static TransformBuilder builder() {
        return new TransformBuilder();
    }

    public static final class TransformBuilder {
        private String withKeyPrefix;
        private String withKeySuffix;
        private ReplaceKeyPrefix replaceKeyPrefix;

        private TransformBuilder() {
        }

        public TransformBuilder withKeyPrefix(String withKeyPrefix) {
            this.withKeyPrefix = withKeyPrefix;
            return this;
        }

        public TransformBuilder withKeySuffix(String withKeySuffix) {
            this.withKeySuffix = withKeySuffix;
            return this;
        }

        public TransformBuilder replaceKeyPrefix(ReplaceKeyPrefix replaceKeyPrefix) {
            this.replaceKeyPrefix = replaceKeyPrefix;
            return this;
        }

        public Transform build() {
            Transform transform = new Transform();
            transform.setWithKeyPrefix(withKeyPrefix);
            transform.setWithKeySuffix(withKeySuffix);
            transform.setReplaceKeyPrefix(replaceKeyPrefix);
            return transform;
        }
    }
}
