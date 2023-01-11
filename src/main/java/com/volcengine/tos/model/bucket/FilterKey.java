package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FilterKey {
    @JsonProperty("FilterRules")
    private List<FilterRule> rules;

    public List<FilterRule> getRules() {
        return rules;
    }

    public FilterKey setRules(List<FilterRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "FilterKey{" +
                "rules=" + rules +
                '}';
    }

    public static FilterKeyBuilder builder() {
        return new FilterKeyBuilder();
    }

    public static final class FilterKeyBuilder {
        private List<FilterRule> rules;

        private FilterKeyBuilder() {
        }

        public FilterKeyBuilder rules(List<FilterRule> rules) {
            this.rules = rules;
            return this;
        }

        public FilterKey build() {
            FilterKey filterKey = new FilterKey();
            filterKey.setRules(rules);
            return filterKey;
        }
    }
}
