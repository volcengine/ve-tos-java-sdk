package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDocument {
    @JsonProperty("Key")
    private String key;

    public String getKey() {
        return key;
    }

    public ErrorDocument setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorDocument{" +
                "key='" + key + '\'' +
                '}';
    }

    public static ErrorDocumentBuilder builder() {
        return new ErrorDocumentBuilder();
    }

    public static final class ErrorDocumentBuilder {
        private String key;

        private ErrorDocumentBuilder() {
        }

        public ErrorDocumentBuilder key(String key) {
            this.key = key;
            return this;
        }

        public ErrorDocument build() {
            ErrorDocument errorDocument = new ErrorDocument();
            errorDocument.setKey(key);
            return errorDocument;
        }
    }
}
