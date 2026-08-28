package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationKafka {
    @JsonProperty("BootstrapServers")
    String bootstrapServers;
    @JsonProperty("GroupId")
    String groupId;
    @JsonProperty("Topic")
    String topic;
    @JsonProperty("SecurityProtocol")
    String securityProtocol;
    @JsonProperty("SaslMechanism")
    String saslMechanism;
    @JsonProperty("Username")
    String username;
    @JsonProperty("Password")
    String password;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public DestinationKafka setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public DestinationKafka setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public DestinationKafka setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public DestinationKafka setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
        return this;
    }

    public String getSaslMechanism() {
        return saslMechanism;
    }

    public DestinationKafka setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DestinationKafka setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DestinationKafka setPassword(String password) {
        this.password = password;
        return this;
    }

    public static DestinationKafkaBuilder builder() {
        return new DestinationKafkaBuilder();
    }

    public static final class DestinationKafkaBuilder {
        private String bootstrapServers;
        private String groupId;
        private String topic;
        private String securityProtocol;
        private String saslMechanism;
        private String username;
        private String password;

        private DestinationKafkaBuilder() {
        }

        public DestinationKafkaBuilder bootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
            return this;
        }

        public DestinationKafkaBuilder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public DestinationKafkaBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public DestinationKafkaBuilder securityProtocol(String securityProtocol) {
            this.securityProtocol = securityProtocol;
            return this;
        }

        public DestinationKafkaBuilder saslMechanism(String saslMechanism) {
            this.saslMechanism = saslMechanism;
            return this;
        }

        public DestinationKafkaBuilder username(String username) {
            this.username = username;
            return this;
        }

        public DestinationKafkaBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DestinationKafka build() {
            DestinationKafka destinationKafka = new DestinationKafka();
            destinationKafka.setBootstrapServers(bootstrapServers);
            destinationKafka.setGroupId(groupId);
            destinationKafka.setTopic(topic);
            destinationKafka.setSecurityProtocol(securityProtocol);
            destinationKafka.setSaslMechanism(saslMechanism);
            destinationKafka.setUsername(username);
            destinationKafka.setPassword(password);
            return destinationKafka;
        }
    }
}
