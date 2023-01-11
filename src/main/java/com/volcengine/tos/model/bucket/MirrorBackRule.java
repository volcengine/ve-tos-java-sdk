package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MirrorBackRule {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Condition")
    private Condition condition;
    @JsonProperty("Redirect")
    private Redirect redirect;

    public String getId() {
        return id;
    }

    public MirrorBackRule setId(String id) {
        this.id = id;
        return this;
    }

    public Condition getCondition() {
        return condition;
    }

    public MirrorBackRule setCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public MirrorBackRule setRedirect(Redirect redirect) {
        this.redirect = redirect;
        return this;
    }

    @Override
    public String toString() {
        return "MirrorBackRule{" +
                "id='" + id + '\'' +
                ", condition=" + condition +
                ", redirect=" + redirect +
                '}';
    }

    public static MirrorBackRuleBuilder builder() {
        return new MirrorBackRuleBuilder();
    }

    public static final class MirrorBackRuleBuilder {
        private String id;
        private Condition condition;
        private Redirect redirect;

        private MirrorBackRuleBuilder() {
        }

        public MirrorBackRuleBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MirrorBackRuleBuilder condition(Condition condition) {
            this.condition = condition;
            return this;
        }

        public MirrorBackRuleBuilder redirect(Redirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public MirrorBackRule build() {
            MirrorBackRule mirrorBackRule = new MirrorBackRule();
            mirrorBackRule.setId(id);
            mirrorBackRule.setCondition(condition);
            mirrorBackRule.setRedirect(redirect);
            return mirrorBackRule;
        }
    }
}
