package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class GetQosPolicyInput extends GenericInput {
    private String accountId;

    public GetQosPolicyInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public GetQosPolicyInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return "GetQosPolicyInput{" +
                "accountId=" + accountId + '}';
    }
}
