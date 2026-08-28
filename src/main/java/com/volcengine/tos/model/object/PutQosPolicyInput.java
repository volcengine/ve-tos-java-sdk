package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class PutQosPolicyInput extends GenericInput {
    private String accountId;

    private String policy;

    public PutQosPolicyInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public PutQosPolicyInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public PutQosPolicyInput setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    @Override
    public String toString() {
        return "PutQosPolicyInput{"
                + "accountId='" + accountId + '\''
                + ", policy='" + policy + '\'' +
                '}';
    }
}
