package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationHttpServer {
    @JsonProperty("Url")
    String url;

    public String getUrl() {
        return url;
    }

    public DestinationHttpServer setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "DestinationHttpServer{" +
                "url='" + url + '\'' +
                '}';
    }

    public static DestinationHttpServerBuilder builder() {
        return new DestinationHttpServerBuilder();
    }

    public static final class DestinationHttpServerBuilder {
        private String url;

        private DestinationHttpServerBuilder() {
        }

        public DestinationHttpServerBuilder url(String url) {
            this.url = url;
            return this;
        }

        public DestinationHttpServer build() {
            DestinationHttpServer destinationHttpServer = new DestinationHttpServer();
            destinationHttpServer.setUrl(url);
            return destinationHttpServer;
        }
    }
}
