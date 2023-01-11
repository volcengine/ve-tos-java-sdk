package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessLogConfiguration {
    @JsonProperty("UseServiceTopic")
    private boolean useServiceTopic;
    @JsonProperty("TLSProjectID")
    private String tlsProjectID;
    @JsonProperty("TLSTopicID")
    private String tlsTopicID;

    public boolean isUseServiceTopic() {
        return useServiceTopic;
    }

    public AccessLogConfiguration setUseServiceTopic(boolean useServiceTopic) {
        this.useServiceTopic = useServiceTopic;
        return this;
    }

    public String getTlsProjectID() {
        return tlsProjectID;
    }

    public AccessLogConfiguration setTlsProjectID(String tlsProjectID) {
        this.tlsProjectID = tlsProjectID;
        return this;
    }

    public String getTlsTopicID() {
        return tlsTopicID;
    }

    public AccessLogConfiguration setTlsTopicID(String tlsTopicID) {
        this.tlsTopicID = tlsTopicID;
        return this;
    }

    @Override
    public String toString() {
        return "AccessLogConfiguration{" +
                "useServiceTopic=" + useServiceTopic +
                ", tlsProjectID='" + tlsProjectID + '\'' +
                ", tlsTopicID='" + tlsTopicID + '\'' +
                '}';
    }

    public static AccessLogConfigurationBuilder builder() {
        return new AccessLogConfigurationBuilder();
    }

    public static final class AccessLogConfigurationBuilder {
        private boolean useServiceTopic;
        private String tlsProjectID;
        private String tlsTopicID;

        private AccessLogConfigurationBuilder() {
        }

        public AccessLogConfigurationBuilder useServiceTopic(boolean useServiceTopic) {
            this.useServiceTopic = useServiceTopic;
            return this;
        }

        public AccessLogConfigurationBuilder tlsProjectID(String tlsProjectID) {
            this.tlsProjectID = tlsProjectID;
            return this;
        }

        public AccessLogConfigurationBuilder tlsTopicID(String tlsTopicID) {
            this.tlsTopicID = tlsTopicID;
            return this;
        }

        public AccessLogConfiguration build() {
            AccessLogConfiguration accessLogConfiguration = new AccessLogConfiguration();
            accessLogConfiguration.setUseServiceTopic(useServiceTopic);
            accessLogConfiguration.setTlsProjectID(tlsProjectID);
            accessLogConfiguration.setTlsTopicID(tlsTopicID);
            return accessLogConfiguration;
        }
    }
}
