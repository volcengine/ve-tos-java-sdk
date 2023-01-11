package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RealTimeLogConfiguration {
    @JsonProperty("Role")
    private String role;
    @JsonProperty("AccessLogConfiguration")
    private AccessLogConfiguration configuration;

    public String getRole() {
        return role;
    }

    public RealTimeLogConfiguration setRole(String role) {
        this.role = role;
        return this;
    }

    public AccessLogConfiguration getConfiguration() {
        return configuration;
    }

    public RealTimeLogConfiguration setConfiguration(AccessLogConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String toString() {
        return "RealTimeLogConfiguration{" +
                "role='" + role + '\'' +
                ", configuration=" + configuration +
                '}';
    }

    public static RealTimeLogConfigurationBuilder builder() {
        return new RealTimeLogConfigurationBuilder();
    }

    public static final class RealTimeLogConfigurationBuilder {
        private String role;
        private AccessLogConfiguration configuration;

        private RealTimeLogConfigurationBuilder() {
        }

        public RealTimeLogConfigurationBuilder role(String role) {
            this.role = role;
            return this;
        }

        public RealTimeLogConfigurationBuilder configuration(AccessLogConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public RealTimeLogConfiguration build() {
            RealTimeLogConfiguration realTimeLogConfiguration = new RealTimeLogConfiguration();
            realTimeLogConfiguration.setRole(role);
            realTimeLogConfiguration.setConfiguration(configuration);
            return realTimeLogConfiguration;
        }
    }
}
