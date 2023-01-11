package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ProtocolType;

public class RoutingRuleRedirect {
    @JsonProperty("Protocol")
    private ProtocolType protocol;
    @JsonProperty("HostName")
    private String hostname;
    @JsonProperty("ReplaceKeyPrefixWith")
    private String replaceKeyPrefixWith;
    @JsonProperty("ReplaceKeyWith")
    private String replaceKeyWith;
    @JsonProperty("HttpRedirectCode")
    private int httpRedirectCode;

    public ProtocolType getProtocol() {
        return protocol;
    }

    public RoutingRuleRedirect setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public RoutingRuleRedirect setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getReplaceKeyPrefixWith() {
        return replaceKeyPrefixWith;
    }

    public RoutingRuleRedirect setReplaceKeyPrefixWith(String replaceKeyPrefixWith) {
        this.replaceKeyPrefixWith = replaceKeyPrefixWith;
        return this;
    }

    public String getReplaceKeyWith() {
        return replaceKeyWith;
    }

    public RoutingRuleRedirect setReplaceKeyWith(String replaceKeyWith) {
        this.replaceKeyWith = replaceKeyWith;
        return this;
    }

    public int getHttpRedirectCode() {
        return httpRedirectCode;
    }

    public RoutingRuleRedirect setHttpRedirectCode(int httpRedirectCode) {
        this.httpRedirectCode = httpRedirectCode;
        return this;
    }

    @Override
    public String toString() {
        return "RoutingRuleRedirect{" +
                "protocol=" + protocol +
                ", hostname='" + hostname + '\'' +
                ", replaceKeyPrefixWith='" + replaceKeyPrefixWith + '\'' +
                ", replaceKeyWith='" + replaceKeyWith + '\'' +
                ", httpRedirectCode=" + httpRedirectCode +
                '}';
    }

    public static RoutingRuleRedirectBuilder builder() {
        return new RoutingRuleRedirectBuilder();
    }

    public static final class RoutingRuleRedirectBuilder {
        private ProtocolType protocol;
        private String hostname;
        private String replaceKeyPrefixWith;
        private String replaceKeyWith;
        private int httpRedirectCode;

        private RoutingRuleRedirectBuilder() {
        }

        public RoutingRuleRedirectBuilder protocol(ProtocolType protocol) {
            this.protocol = protocol;
            return this;
        }

        public RoutingRuleRedirectBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public RoutingRuleRedirectBuilder replaceKeyPrefixWith(String replaceKeyPrefixWith) {
            this.replaceKeyPrefixWith = replaceKeyPrefixWith;
            return this;
        }

        public RoutingRuleRedirectBuilder replaceKeyWith(String replaceKeyWith) {
            this.replaceKeyWith = replaceKeyWith;
            return this;
        }

        public RoutingRuleRedirectBuilder httpRedirectCode(int httpRedirectCode) {
            this.httpRedirectCode = httpRedirectCode;
            return this;
        }

        public RoutingRuleRedirect build() {
            RoutingRuleRedirect routingRuleRedirect = new RoutingRuleRedirect();
            routingRuleRedirect.setProtocol(protocol);
            routingRuleRedirect.setHostname(hostname);
            routingRuleRedirect.setReplaceKeyPrefixWith(replaceKeyPrefixWith);
            routingRuleRedirect.setReplaceKeyWith(replaceKeyWith);
            routingRuleRedirect.setHttpRedirectCode(httpRedirectCode);
            return routingRuleRedirect;
        }
    }
}
