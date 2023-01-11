package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Filter {
    @JsonProperty("TOSKey")
    private FilterKey key;

    public FilterKey getKey() {
        return key;
    }

    public Filter setKey(FilterKey key) {
        this.key = key;
        return this;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "key=" + key +
                '}';
    }

    public static FilterBuilder builder() {
        return new FilterBuilder();
    }

    public static final class FilterBuilder {
        private FilterKey key;

        private FilterBuilder() {
        }

        public FilterBuilder key(FilterKey key) {
            this.key = key;
            return this;
        }

        public Filter build() {
            Filter filter = new Filter();
            filter.setKey(key);
            return filter;
        }
    }
}
