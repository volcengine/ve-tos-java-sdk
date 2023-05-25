package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RocketMQConfiguration {
    @JsonProperty("RuleId")
    private String id;
    @JsonProperty("Role")
    private String role;
    @JsonProperty("Events")
    private List<String> events;
    @JsonProperty("Filter")
    private Filter filter;
    @JsonProperty("RocketMQ")
    private RocketMQConf rocketMQ;

    public String getId() {
        return id;
    }

    public RocketMQConfiguration setId(String id) {
        this.id = id;
        return this;
    }

    public String getRole() {
        return role;
    }

    public RocketMQConfiguration setRole(String role) {
        this.role = role;
        return this;
    }

    public List<String> getEvents() {
        return events;
    }

    public RocketMQConfiguration setEvents(List<String> events) {
        this.events = events;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public RocketMQConfiguration setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public RocketMQConf getRocketMQ() {
        return rocketMQ;
    }

    public RocketMQConfiguration setRocketMQ(RocketMQConf rocketMQ) {
        this.rocketMQ = rocketMQ;
        return this;
    }

    @Override
    public String toString() {
        return "RocketMQConfiguration{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", events=" + events +
                ", filter=" + filter +
                ", rocketMQ=" + rocketMQ +
                '}';
    }

    public static RocketMQConfigurationBuilder builder() {
        return new RocketMQConfigurationBuilder();
    }

    public static final class RocketMQConfigurationBuilder {
        private String id;
        private String role;
        private List<String> events;
        private Filter filter;
        private RocketMQConf rocketMQ;

        private RocketMQConfigurationBuilder() {
        }

        public RocketMQConfigurationBuilder id(String id) {
            this.id = id;
            return this;
        }

        public RocketMQConfigurationBuilder role(String role) {
            this.role = role;
            return this;
        }

        public RocketMQConfigurationBuilder events(List<String> events) {
            this.events = events;
            return this;
        }

        public RocketMQConfigurationBuilder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public RocketMQConfigurationBuilder rocketMQ(RocketMQConf rocketMQ) {
            this.rocketMQ = rocketMQ;
            return this;
        }

        public RocketMQConfiguration build() {
            RocketMQConfiguration rocketMQConfiguration = new RocketMQConfiguration();
            rocketMQConfiguration.setId(id);
            rocketMQConfiguration.setRole(role);
            rocketMQConfiguration.setEvents(events);
            rocketMQConfiguration.setFilter(filter);
            rocketMQConfiguration.setRocketMQ(rocketMQ);
            return rocketMQConfiguration;
        }
    }
}
