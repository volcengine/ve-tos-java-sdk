package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoutingRule {
    @JsonProperty("Condition")
    private RoutingRuleCondition condition;
    @JsonProperty("Redirect")
    private RoutingRuleRedirect redirect;

    public RoutingRuleCondition getCondition() {
        return condition;
    }

    public RoutingRule setCondition(RoutingRuleCondition condition) {
        this.condition = condition;
        return this;
    }

    public RoutingRuleRedirect getRedirect() {
        return redirect;
    }

    public RoutingRule setRedirect(RoutingRuleRedirect redirect) {
        this.redirect = redirect;
        return this;
    }

    @Override
    public String toString() {
        return "RoutingRule{" +
                "condition=" + condition +
                ", redirect=" + redirect +
                '}';
    }

    public static RoutingRuleBuilder builder() {
        return new RoutingRuleBuilder();
    }

    public static final class RoutingRuleBuilder {
        private RoutingRuleCondition condition;
        private RoutingRuleRedirect redirect;

        private RoutingRuleBuilder() {
        }

        public RoutingRuleBuilder condition(RoutingRuleCondition condition) {
            this.condition = condition;
            return this;
        }

        public RoutingRuleBuilder redirect(RoutingRuleRedirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public RoutingRule build() {
            RoutingRule routingRule = new RoutingRule();
            routingRule.setCondition(condition);
            routingRule.setRedirect(redirect);
            return routingRule;
        }
    }
}
