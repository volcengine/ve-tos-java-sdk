package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class DeleteQosPolicyInput extends GenericInput {
    String accountId;

    public DeleteQosPolicyInput() {
    }

    public DeleteQosPolicyInput(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public DeleteQosPolicyInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteQosPolicyInput{" +
                "accountId='" + accountId + '\'' +
                '}';
    }
}
