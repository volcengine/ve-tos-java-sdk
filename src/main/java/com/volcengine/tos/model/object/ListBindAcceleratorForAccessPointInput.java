package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListBindAcceleratorForAccessPointInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String accessPointName;

    public ListBindAcceleratorForAccessPointInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public ListBindAcceleratorForAccessPointInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public ListBindAcceleratorForAccessPointInput setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
        return this;
    }

    @Override
    public String toString() {
        return "ListBindAcceleratorForAccessPointInput{" +
                "accountId='" + accountId + '\'' +
                ", accessPointName='" + accessPointName + '\'' +
                '}';
    }

    public static ListBindAcceleratorForAccessPointInputBuilder builder() {
        return new ListBindAcceleratorForAccessPointInputBuilder();
    }

    public static final class ListBindAcceleratorForAccessPointInputBuilder {
        private String accountId;
        private String accessPointName;

        private ListBindAcceleratorForAccessPointInputBuilder() {
        }

        public ListBindAcceleratorForAccessPointInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public ListBindAcceleratorForAccessPointInputBuilder accessPointName(String accessPointName) {
            this.accessPointName = accessPointName;
            return this;
        }

        public ListBindAcceleratorForAccessPointInput build() {
            ListBindAcceleratorForAccessPointInput input = new ListBindAcceleratorForAccessPointInput();
            input.setAccountId(accountId);
            input.setAccessPointName(accessPointName);
            return input;
        }
    }
}
