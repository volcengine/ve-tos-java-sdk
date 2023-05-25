package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RocketMQConf {
    @JsonProperty("InstanceId")
    private String instanceID;
    @JsonProperty("Topic")
    private String topic;
    @JsonProperty("AccessKeyId")
    private String accessKeyID;

    public String getInstanceID() {
        return instanceID;
    }

    public RocketMQConf setInstanceID(String instanceID) {
        this.instanceID = instanceID;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public RocketMQConf setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public RocketMQConf setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
        return this;
    }

    @Override
    public String toString() {
        return "RocketMQConf{" +
                "instanceID='" + instanceID + '\'' +
                ", topic='" + topic + '\'' +
                ", accessKeyID='" + accessKeyID + '\'' +
                '}';
    }

    public static RocketMQConfBuilder builder() {
        return new RocketMQConfBuilder();
    }

    public static final class RocketMQConfBuilder {
        private String instanceID;
        private String topic;
        private String accessKeyID;

        private RocketMQConfBuilder() {
        }

        public RocketMQConfBuilder instanceID(String instanceID) {
            this.instanceID = instanceID;
            return this;
        }

        public RocketMQConfBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public RocketMQConfBuilder accessKeyID(String accessKeyID) {
            this.accessKeyID = accessKeyID;
            return this;
        }

        public RocketMQConf build() {
            RocketMQConf rocketMQConf = new RocketMQConf();
            rocketMQConf.setInstanceID(instanceID);
            rocketMQConf.setTopic(topic);
            rocketMQConf.setAccessKeyID(accessKeyID);
            return rocketMQConf;
        }
    }
}
