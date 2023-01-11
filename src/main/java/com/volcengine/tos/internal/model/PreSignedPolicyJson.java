package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.object.PolicySignatureCondition;

import java.util.List;

public class PreSignedPolicyJson {
    @JsonProperty("conditions")
    private List<PolicySignatureCondition> conditions;

    public List<PolicySignatureCondition> getConditions() {
        return conditions;
    }

    public PreSignedPolicyJson setConditions(List<PolicySignatureCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedPolicyJson{" +
                "conditions=" + conditions +
                '}';
    }
}
