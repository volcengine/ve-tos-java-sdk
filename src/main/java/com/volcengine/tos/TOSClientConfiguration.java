package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.credential.CredentialsProvider;
import com.volcengine.tos.credential.EcsCredentialsProvider;
import com.volcengine.tos.credential.EnvCredentialsProvider;
import com.volcengine.tos.credential.StaticCredentialsProvider;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.CredentialsProviderWrapper;
import com.volcengine.tos.transport.TransportConfig;

import java.util.HashMap;
import java.util.Map;

public class TOSClientConfiguration {
    private Credentials credentials;
    private CredentialsProvider credentialsProvider;
    private String endpoint;
    private String region;
    private TransportConfig transportConfig;
    /**
     * 客户端根据对象名后缀自动识别 Content-Type
     */
    private boolean clientAutoRecognizeContentType = Consts.DEFAULT_AUTO_RECOGNIZE_CONTENT_TYPE;
    /**
     * 客户端对上传对象开启 CRC 校验
     */
    private boolean enableCrc = Consts.DEFAULT_ENABLE_CRC;
    private boolean disableTrailerHeader = Consts.DEFAULT_DISABLE_TRAILER_HEADER;

    /**
     * 是否是自定义域名
     */
    private boolean isCustomDomain = false;
    private boolean disableEncodingMeta;
    private String userAgentProductName;
    private String userAgentSoftName;
    private String userAgentSoftVersion;
    private Map<String, String> userAgentCustomizedKeyValues;

    private TOSClientConfiguration() {
    }

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

    @Deprecated
    public Credentials getCredentials() {
        return this.credentials;
    }

    public CredentialsProvider getCredentialsProvider() {
        return this.credentialsProvider;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Deprecated
    public TOSClientConfiguration setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public TOSClientConfiguration setCredentialsProvider(CredentialsProvider credentialsProvider) {
        if (credentialsProvider != null) {
            if (!(credentialsProvider instanceof StaticCredentialsProvider)
                    && !(credentialsProvider instanceof EnvCredentialsProvider)
                    && !(credentialsProvider instanceof EcsCredentialsProvider)
                    && !(credentialsProvider instanceof CredentialsProviderWrapper)) {
                credentialsProvider = new CredentialsProviderWrapper(credentialsProvider);
            }
        }
        this.credentialsProvider = credentialsProvider;
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

    public boolean isDisableTrailerHeader() {
        return disableTrailerHeader;
    }

    public void setDisableTrailerHeader(boolean disableTrailerHeader) {
        this.disableTrailerHeader = disableTrailerHeader;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public TOSClientConfiguration setCustomDomain(boolean customDomain) {
        isCustomDomain = customDomain;
        return this;
    }

    public boolean isDisableEncodingMeta() {
        return disableEncodingMeta;
    }

    public TOSClientConfiguration setDisableEncodingMeta(boolean disableEncodingMeta) {
        this.disableEncodingMeta = disableEncodingMeta;
        return this;
    }

    public String getUserAgentProductName() {
        return this.userAgentProductName;
    }

    public TOSClientConfiguration setUserAgentProductName(String userAgentProductName) {
        this.userAgentProductName = userAgentProductName;
        return this;
    }

    public String getUserAgentSoftName() {
        return this.userAgentSoftName;
    }

    public TOSClientConfiguration setUserAgentSoftName(String userAgentSoftName) {
        this.userAgentSoftName = userAgentSoftName;
        return this;
    }

    public String getUserAgentSoftVersion() {
        return this.userAgentSoftVersion;
    }

    public TOSClientConfiguration setUserAgentSoftVersion(String userAgentSoftVersion) {
        this.userAgentSoftVersion = userAgentSoftVersion;
        return this;
    }

    public Map<String, String> getUserAgentCustomizedKeyValues() {
        return this.userAgentCustomizedKeyValues;
    }

    public TOSClientConfiguration setUserAgentCustomizedKeyValues(Map<String, String> userAgentCustomizedKeyValues) {
        if (userAgentCustomizedKeyValues != null) {
            this.userAgentCustomizedKeyValues = new HashMap<>(userAgentCustomizedKeyValues.size());
            for (Map.Entry<String, String> e : userAgentCustomizedKeyValues.entrySet()) {
                this.userAgentCustomizedKeyValues.put(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    public static TosClientConfigurationBuilder builder() {
        return new TosClientConfigurationBuilder();
    }

    public static final class TosClientConfigurationBuilder {
        private Credentials credentials;
        private CredentialsProvider credentialsProvider;
        private String endpoint;
        private String region;
        private TransportConfig transportConfig = TransportConfig.builder().build();
        private boolean clientAutoRecognizeContentType = Consts.DEFAULT_AUTO_RECOGNIZE_CONTENT_TYPE;
        private boolean enableCrc = Consts.DEFAULT_ENABLE_CRC;
        private boolean disableTrailerHeader = Consts.DEFAULT_DISABLE_TRAILER_HEADER;
        private boolean isCustomDomain = false;
        private boolean disableEncodingMeta;
        private String userAgentProductName;
        private String userAgentSoftName;
        private String userAgentSoftVersion;
        private Map<String, String> userAgentCustomizedKeyValues;

        private TosClientConfigurationBuilder() {
        }

        @Deprecated
        public TosClientConfigurationBuilder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public TosClientConfigurationBuilder credentialsProvider(CredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
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

        public TosClientConfigurationBuilder disableTrailerHeader(boolean disableTrailerHeader) {
            this.disableTrailerHeader = disableTrailerHeader;
            return this;
        }

        public TosClientConfigurationBuilder isCustomDomain(boolean isCustomDomain) {
            this.isCustomDomain = isCustomDomain;
            return this;
        }

        public TosClientConfigurationBuilder disableEncodingMeta(boolean disableEncodingMeta) {
            this.disableEncodingMeta = disableEncodingMeta;
            return this;
        }

        public TosClientConfigurationBuilder userAgentProductName(String userAgentProductName) {
            this.userAgentProductName = userAgentProductName;
            return this;
        }

        public TosClientConfigurationBuilder userAgentSoftName(String userAgentSoftName) {
            this.userAgentSoftName = userAgentSoftName;
            return this;
        }

        public TosClientConfigurationBuilder userAgentSoftVersion(String userAgentSoftVersion) {
            this.userAgentSoftVersion = userAgentSoftVersion;
            return this;
        }

        public TosClientConfigurationBuilder userAgentCustomizedKeyValues(Map<String, String> userAgentCustomizedKeyValues) {
            this.userAgentCustomizedKeyValues = userAgentCustomizedKeyValues;
            return this;
        }

        public TOSClientConfiguration build() {
            TOSClientConfiguration tosClientConfiguration = new TOSClientConfiguration();
            tosClientConfiguration.enableCrc = this.enableCrc;
            tosClientConfiguration.disableTrailerHeader = this.disableTrailerHeader;
            tosClientConfiguration.endpoint = this.endpoint;
            tosClientConfiguration.transportConfig = this.transportConfig;
            tosClientConfiguration.credentials = this.credentials;
            tosClientConfiguration.setCredentialsProvider(this.credentialsProvider);
            tosClientConfiguration.region = this.region;
            tosClientConfiguration.clientAutoRecognizeContentType = this.clientAutoRecognizeContentType;
            tosClientConfiguration.isCustomDomain = this.isCustomDomain;
            tosClientConfiguration.disableEncodingMeta = this.disableEncodingMeta;
            tosClientConfiguration.setUserAgentProductName(this.userAgentProductName);
            tosClientConfiguration.setUserAgentSoftName(this.userAgentSoftName);
            tosClientConfiguration.setUserAgentSoftVersion(this.userAgentSoftVersion);
            tosClientConfiguration.setUserAgentCustomizedKeyValues(this.userAgentCustomizedKeyValues);
            return tosClientConfiguration;
        }
    }
}
