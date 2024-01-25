package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NotificationDestination {
    @JsonProperty("RocketMQ")
    private List<DestinationRocketMQ> rocketMQ;
    @JsonProperty("VeFaaS")
    private List<DestinationVeFaaS> veFaaS;

    public List<DestinationRocketMQ> getRocketMQ() {
        return rocketMQ;
    }

    public NotificationDestination setRocketMQ(List<DestinationRocketMQ> rocketMQ) {
        this.rocketMQ = rocketMQ;
        return this;
    }

    public List<DestinationVeFaaS> getVeFaaS() {
        return veFaaS;
    }

    public NotificationDestination setVeFaaS(List<DestinationVeFaaS> veFaaS) {
        this.veFaaS = veFaaS;
        return this;
    }

    @Override
    public String toString() {
        return "NotificationDestination{" +
                "rocketMQ=" + rocketMQ +
                ", veFaaS=" + veFaaS +
                '}';
    }

    public static NotificationDestinationBuilder builder() {
        return new NotificationDestinationBuilder();
    }

    public static final class NotificationDestinationBuilder {
        private List<DestinationRocketMQ> rocketMQ;
        private List<DestinationVeFaaS> veFaaS;

        private NotificationDestinationBuilder() {
        }

        public NotificationDestinationBuilder rocketMQ(List<DestinationRocketMQ> rocketMQ) {
            this.rocketMQ = rocketMQ;
            return this;
        }

        public NotificationDestinationBuilder veFaaS(List<DestinationVeFaaS> veFaaS) {
            this.veFaaS = veFaaS;
            return this;
        }

        public NotificationDestination build() {
            NotificationDestination notificationDestination = new NotificationDestination();
            notificationDestination.setRocketMQ(rocketMQ);
            notificationDestination.setVeFaaS(veFaaS);
            return notificationDestination;
        }
    }
}
