package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationVeFaaS {
    @JsonProperty("FunctionId")
    String functionId;

    public String getFunctionId() {
        return functionId;
    }

    public DestinationVeFaaS setFunctionId(String functionId) {
        this.functionId = functionId;
        return this;
    }

    @Override
    public String toString() {
        return "DestinationVeFaaS{" +
                "functionId='" + functionId + '\'' +
                '}';
    }

    public static DestinationVeFaaSBuilder builder() {
        return new DestinationVeFaaSBuilder();
    }

    public static final class DestinationVeFaaSBuilder {
        private String functionId;

        private DestinationVeFaaSBuilder() {
        }

        public DestinationVeFaaSBuilder functionId(String functionId) {
            this.functionId = functionId;
            return this;
        }

        public DestinationVeFaaS build() {
            DestinationVeFaaS destinationVeFaaS = new DestinationVeFaaS();
            destinationVeFaaS.setFunctionId(functionId);
            return destinationVeFaaS;
        }
    }
}
