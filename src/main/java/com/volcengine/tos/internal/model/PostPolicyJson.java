package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.object.PostSignatureCondition;

import java.util.List;

public class PostPolicyJson {
    @JsonProperty("conditions")
    private List<PostSignatureCondition> conditions;
    @JsonProperty("expiration")
    private String expiration;

    public List<PostSignatureCondition> getConditions() {
        return conditions;
    }

    public PostPolicyJson setConditions(List<PostSignatureCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public String getExpiration() {
        return expiration;
    }

    public PostPolicyJson setExpiration(String expiration) {
        this.expiration = expiration;
        return this;
    }

    @Override
    public String toString() {
        return "PostPolicyJson{" +
                "conditions=" + conditions +
                ", expiration='" + expiration + '\'' +
                '}';
    }
}
