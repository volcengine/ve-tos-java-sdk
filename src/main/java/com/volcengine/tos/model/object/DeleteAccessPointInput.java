package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class DeleteAccessPointInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String accessPointName;

    public DeleteAccessPointInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public DeleteAccessPointInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public DeleteAccessPointInput setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteAccessPointInput{" +
                "accountId='" + accountId + '\'' +
                ", accessPointName='" + accessPointName + '\'' +
                '}';
    }

    public static DeleteAccessPointInputBuilder builder() {
        return new DeleteAccessPointInputBuilder();
    }

    public static final class DeleteAccessPointInputBuilder {
        private String accountId;
        private String accessPointName;

        private DeleteAccessPointInputBuilder() {
        }

        public DeleteAccessPointInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public DeleteAccessPointInputBuilder accessPointName(String accessPointName) {
            this.accessPointName = accessPointName;
            return this;
        }

        public DeleteAccessPointInput build() {
            DeleteAccessPointInput input = new DeleteAccessPointInput();
            input.setAccountId(accountId);
            input.setAccessPointName(accessPointName);
            return input;
        }
    }
}
