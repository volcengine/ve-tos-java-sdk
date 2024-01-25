package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationRocketMQ {
    @JsonProperty("Role")
    String role;
    @JsonProperty("InstanceId")
    String instanceId;
    @JsonProperty("Topic")
    String topic;
    @JsonProperty("AccessKeyId")
    String accessKeyId;

    public String getRole() {
        return role;
    }

    public DestinationRocketMQ setRole(String role) {
        this.role = role;
        return this;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public DestinationRocketMQ setInstanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public DestinationRocketMQ setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public DestinationRocketMQ setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public static DestinationRocketMQBuilder builder() {
        return new DestinationRocketMQBuilder();
    }

    public static final class DestinationRocketMQBuilder {
        private String role;
        private String instanceId;
        private String topic;
        private String accessKeyId;

        private DestinationRocketMQBuilder() {
        }

        public DestinationRocketMQBuilder role(String role) {
            this.role = role;
            return this;
        }

        public DestinationRocketMQBuilder instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public DestinationRocketMQBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public DestinationRocketMQBuilder accessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
            return this;
        }

        public DestinationRocketMQ build() {
            DestinationRocketMQ destinationRocketMQ = new DestinationRocketMQ();
            destinationRocketMQ.setRole(role);
            destinationRocketMQ.setInstanceId(instanceId);
            destinationRocketMQ.setTopic(topic);
            destinationRocketMQ.setAccessKeyId(accessKeyId);
            return destinationRocketMQ;
        }
    }
}


