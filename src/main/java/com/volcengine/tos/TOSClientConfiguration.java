package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.transport.TransportConfig;

public class TOSClientConfiguration {
    private Credentials credentials;
    private String endpoint;
    private String region;
    private TransportConfig transportConfig;

    private TOSClientConfiguration(){}

    /**
     * 客户端根据对象名后缀自动识别 Content-Type
     */
    private boolean clientAutoRecognizeContentType = Consts.DEFAULT_AUTO_RECOGNIZE_CONTENT_TYPE;
    /**
     * 客户端对上传对象开启 CRC 校验
     */
    private boolean enableCrc = Consts.DEFAULT_ENABLE_CRC;

    /**
     * 是否是自定义域名
     */
    private boolean isCustomDomain = false;

    public boolean isClientAutoRecognizeContentType() {
        return clientAutoRecognizeContentType;
    }

    public boolean isEnableCrc() {
        return enableCrc;
    }

    public TransportConfig getTransportConfig() {
        return this.transportConfig;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public String getRegion() {
        return this.region;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public TOSClientConfiguration setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public TOSClientConfiguration setRegion(String region) {
        this.region = region;
        return this;
    }

    public TOSClientConfiguration setTransportConfig(TransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }

    public TOSClientConfiguration setClientAutoRecognizeContentType(boolean clientAutoRecognizeContentType) {
        this.clientAutoRecognizeContentType = clientAutoRecognizeContentType;
        return this;
    }

    public TOSClientConfiguration setEnableCrc(boolean enableCrc) {
        this.enableCrc = enableCrc;
        return this;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public TOSClientConfiguration setCustomDomain(boolean customDomain) {
        isCustomDomain = customDomain;
        return this;
    }

    public static TosClientConfigurationBuilder builder() {
        return new TosClientConfigurationBuilder();
    }

    public static final class TosClientConfigurationBuilder {
        private Credentials credentials;
        private String endpoint;
        private String region;
        private TransportConfig transportConfig = TransportConfig.builder().build();
        private boolean clientAutoRecognizeContentType = Consts.DEFAULT_AUTO_RECOGNIZE_CONTENT_TYPE;
        private boolean enableCrc = Consts.DEFAULT_ENABLE_CRC;
        private boolean isCustomDomain = false;

        private TosClientConfigurationBuilder() {
        }

        public TosClientConfigurationBuilder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public TosClientConfigurationBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public TosClientConfigurationBuilder region(String region) {
            this.region = region;
            return this;
        }

        public TosClientConfigurationBuilder transportConfig(TransportConfig transportConfig) {
            this.transportConfig = transportConfig;
            return this;
        }

        public TosClientConfigurationBuilder clientAutoRecognizeContentType(boolean clientAutoRecognizeContentType) {
            this.clientAutoRecognizeContentType = clientAutoRecognizeContentType;
            return this;
        }

        public TosClientConfigurationBuilder enableCrc(boolean enableCrc) {
            this.enableCrc = enableCrc;
            return this;
        }

        public TosClientConfigurationBuilder isCustomDomain(boolean isCustomDomain) {
            this.isCustomDomain = isCustomDomain;
            return this;
        }

        public TOSClientConfiguration build() {
            TOSClientConfiguration tosClientConfiguration = new TOSClientConfiguration();
            tosClientConfiguration.enableCrc = this.enableCrc;
            tosClientConfiguration.endpoint = this.endpoint;
            tosClientConfiguration.transportConfig = this.transportConfig;
            tosClientConfiguration.credentials = this.credentials;
            tosClientConfiguration.region = this.region;
            tosClientConfiguration.clientAutoRecognizeContentType = this.clientAutoRecognizeContentType;
            tosClientConfiguration.isCustomDomain = this.isCustomDomain;
            return tosClientConfiguration;
        }
    }
}
