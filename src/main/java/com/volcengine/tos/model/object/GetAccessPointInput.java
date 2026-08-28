package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetAccessPointInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String accessPointName;

    public GetAccessPointInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public GetAccessPointInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public GetAccessPointInput setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
        return this;
    }

    @Override
    public String toString() {
        return "GetAccessPointInput{" +
                "accountId='" + accountId + '\'' +
                ", accessPointName='" + accessPointName + '\'' +
                '}';
    }

    public static GetAccessPointInputBuilder builder() {
        return new GetAccessPointInputBuilder();
    }

    public static final class GetAccessPointInputBuilder {
        private String accountId;
        private String accessPointName;

        private GetAccessPointInputBuilder() {
        }

        public GetAccessPointInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public GetAccessPointInputBuilder accessPointName(String accessPointName) {
            this.accessPointName = accessPointName;
            return this;
        }

        public GetAccessPointInput build() {
            GetAccessPointInput input = new GetAccessPointInput();
            input.setAccountId(accountId);
            input.setAccessPointName(accessPointName);
            return input;
        }
    }
}
