package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ObjectSetTagLifecycleRule {
    @JsonProperty("Tag")
    private Tag tag;

    @JsonProperty("Rules")
    private List<LifecycleRule> rules;

    public Tag getTag() {
        return tag;
    }

    public ObjectSetTagLifecycleRule setTag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public List<LifecycleRule> getRules() {
        return rules;
    }

    public ObjectSetTagLifecycleRule setRules(List<LifecycleRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetTagLifecycleRule{" +
                "tag=" + tag +
                ", rules=" + rules +
                '}';
    }
}
