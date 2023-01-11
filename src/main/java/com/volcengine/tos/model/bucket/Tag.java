package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;

    public String getKey() {
        return key;
    }

    public Tag setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Tag setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public static final class TagBuilder {
        private String key;
        private String value;

        private TagBuilder() {
        }

        public TagBuilder key(String key) {
            this.key = key;
            return this;
        }

        public TagBuilder value(String value) {
            this.value = value;
            return this;
        }

        public Tag build() {
            Tag tag = new Tag();
            tag.setKey(key);
            tag.setValue(value);
            return tag;
        }
    }
}
