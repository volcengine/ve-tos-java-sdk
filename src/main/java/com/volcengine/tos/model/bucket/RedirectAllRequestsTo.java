package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedirectAllRequestsTo {
    @JsonProperty("HostName")
    private String hostname;
    @JsonProperty("Protocol")
    private String protocol;

    public String getHostname() {
        return hostname;
    }

    public RedirectAllRequestsTo setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public RedirectAllRequestsTo setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public String toString() {
        return "RedirectAllRequestsTo{" +
                "hostname='" + hostname + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }

    public static RedirectAllRequestsToBuilder builder() {
        return new RedirectAllRequestsToBuilder();
    }

    public static final class RedirectAllRequestsToBuilder {
        private String hostname;
        private String protocol;

        private RedirectAllRequestsToBuilder() {
        }

        public RedirectAllRequestsToBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public RedirectAllRequestsToBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public RedirectAllRequestsTo build() {
            RedirectAllRequestsTo redirectAllRequestsTo = new RedirectAllRequestsTo();
            redirectAllRequestsTo.setHostname(hostname);
            redirectAllRequestsTo.setProtocol(protocol);
            return redirectAllRequestsTo;
        }
    }
}
