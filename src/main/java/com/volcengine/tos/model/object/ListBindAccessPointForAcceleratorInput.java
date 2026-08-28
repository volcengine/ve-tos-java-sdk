package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListBindAccessPointForAcceleratorInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String acceleratorId;

    public ListBindAccessPointForAcceleratorInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public ListBindAccessPointForAcceleratorInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAcceleratorId() {
        return acceleratorId;
    }

    public ListBindAccessPointForAcceleratorInput setAcceleratorId(String acceleratorId) {
        this.acceleratorId = acceleratorId;
        return this;
    }

    @Override
    public String toString() {
        return "ListBindAccessPointForAcceleratorInput{" +
                "accountId='" + accountId + '\'' +
                ", acceleratorId='" + acceleratorId + '\'' +
                '}';
    }

    public static ListBindAccessPointForAcceleratorInputBuilder builder() {
        return new ListBindAccessPointForAcceleratorInputBuilder();
    }

    public static final class ListBindAccessPointForAcceleratorInputBuilder {
        private String accountId;
        private String acceleratorId;

        private ListBindAccessPointForAcceleratorInputBuilder() {
        }

        public ListBindAccessPointForAcceleratorInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public ListBindAccessPointForAcceleratorInputBuilder acceleratorId(String acceleratorId) {
            this.acceleratorId = acceleratorId;
            return this;
        }

        public ListBindAccessPointForAcceleratorInput build() {
            ListBindAccessPointForAcceleratorInput input = new ListBindAccessPointForAcceleratorInput();
            input.setAccountId(accountId);
            input.setAcceleratorId(acceleratorId);
            return input;
        }
    }
}
