package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplaceKeyPrefix {
    @JsonProperty("KeyPrefix")
    private String keyPrefix;
    @JsonProperty("ReplaceWith")
    private String replaceWith;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public ReplaceKeyPrefix setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
        return this;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public ReplaceKeyPrefix setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
        return this;
    }

    @Override
    public String toString() {
        return "ReplaceKeyPrefix{" +
                "keyPrefix='" + keyPrefix + '\'' +
                ", replaceWith='" + replaceWith + '\'' +
                '}';
    }

    public static ReplaceKeyPrefixBuilder builder() {
        return new ReplaceKeyPrefixBuilder();
    }

    public static final class ReplaceKeyPrefixBuilder {
        private String keyPrefix;
        private String replaceWith;

        private ReplaceKeyPrefixBuilder() {
        }

        public ReplaceKeyPrefixBuilder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        public ReplaceKeyPrefixBuilder replaceWith(String replaceWith) {
            this.replaceWith = replaceWith;
            return this;
        }

        public ReplaceKeyPrefix build() {
            ReplaceKeyPrefix replaceKeyPrefix = new ReplaceKeyPrefix();
            replaceKeyPrefix.setKeyPrefix(keyPrefix);
            replaceKeyPrefix.setReplaceWith(replaceWith);
            return replaceKeyPrefix;
        }
    }
}
