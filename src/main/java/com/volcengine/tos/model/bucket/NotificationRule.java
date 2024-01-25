package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NotificationRule {
    @JsonProperty("RuleId")
    private String ruleId;
    @JsonProperty("Events")
    private List<String> events;
    @JsonProperty("Filter")
    private Filter filter;
    @JsonProperty("Destination")
    private NotificationDestination destination;

    public String getRuleId() {
        return ruleId;
    }

    public NotificationRule setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public List<String> getEvents() {
        return events;
    }

    public NotificationRule setEvents(List<String> events) {
        this.events = events;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public NotificationRule setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public NotificationDestination getDestination() {
        return destination;
    }

    public NotificationRule setDestination(NotificationDestination destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public String toString() {
        return "NotificationRule{" +
                "ruleId='" + ruleId + '\'' +
                ", events=" + events +
                ", filter=" + filter +
                ", destination=" + destination +
                '}';
    }

    public static NotificationRuleBuilder builder() {
        return new NotificationRuleBuilder();
    }

    public static final class NotificationRuleBuilder {
        private String ruleId;
        private List<String> events;
        private Filter filter;
        private NotificationDestination destination;

        private NotificationRuleBuilder() {
        }

        public NotificationRuleBuilder ruleId(String ruleId) {
            this.ruleId = ruleId;
            return this;
        }

        public NotificationRuleBuilder events(List<String> events) {
            this.events = events;
            return this;
        }

        public NotificationRuleBuilder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public NotificationRuleBuilder destination(NotificationDestination destination) {
            this.destination = destination;
            return this;
        }

        public NotificationRule build() {
            NotificationRule notificationRule = new NotificationRule();
            notificationRule.setRuleId(ruleId);
            notificationRule.setEvents(events);
            notificationRule.setFilter(filter);
            notificationRule.setDestination(destination);
            return notificationRule;
        }
    }
}
