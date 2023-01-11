package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterRule {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Value")
    private String value;

    public String getName() {
        return name;
    }

    public FilterRule setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public FilterRule setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "FilterRule{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static FilterRuleBuilder builder() {
        return new FilterRuleBuilder();
    }

    public static final class FilterRuleBuilder {
        private String name;
        private String value;

        private FilterRuleBuilder() {
        }

        public FilterRuleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FilterRuleBuilder value(String value) {
            this.value = value;
            return this;
        }

        public FilterRule build() {
            FilterRule filterRule = new FilterRule();
            filterRule.setName(name);
            filterRule.setValue(value);
            return filterRule;
        }
    }
}
