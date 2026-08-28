package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationKafka {
    @JsonProperty("Role")
    String role;
    @JsonProperty("InstanceId")
    String instanceID;
    @JsonProperty("Topic")
    String topic;
    @JsonProperty("User")
    String user;
    @JsonProperty("Region")
    String region;

    public String getRole() {
        return role;
    }

    public DestinationKafka setRole(String role) {
        this.role = role;
        return this;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public DestinationKafka setInstanceID(String instanceID) {
        this.instanceID = instanceID;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public DestinationKafka setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getUser() {
        return user;
    }

    public DestinationKafka setUser(String user) {
        this.user = user;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public DestinationKafka setRegion(String region) {
        this.region = region;
        return this;
    }

    public static DestinationKafkaBuilder builder() {
        return new DestinationKafkaBuilder();
    }

    public static final class DestinationKafkaBuilder {
        private String role;
        private String instanceID;
        private String topic;
        private String user;
        private String region;

        private DestinationKafkaBuilder() {
        }

        public DestinationKafkaBuilder role(String role) {
            this.role = role;
            return this;
        }

        public DestinationKafkaBuilder instanceID(String instanceID) {
            this.instanceID = instanceID;
            return this;
        }

        public DestinationKafkaBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public DestinationKafkaBuilder user(String user) {
            this.user = user;
            return this;
        }

        public DestinationKafkaBuilder region(String region) {
            this.region = region;
            return this;
        }

        public DestinationKafka build() {
            DestinationKafka destinationKafka = new DestinationKafka();
            destinationKafka.setRole(role);
            destinationKafka.setInstanceID(instanceID);
            destinationKafka.setTopic(topic);
            destinationKafka.setUser(user);
            destinationKafka.setRegion(region);
            return destinationKafka;
        }
    }
}
