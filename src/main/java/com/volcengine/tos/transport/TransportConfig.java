package com.volcengine.tos.transport;

import com.volcengine.tos.internal.Consts;

public class TransportConfig {
    /**
     * 连接池中允许打开的最大 HTTP 连接数，默认 1024
     */
    private int maxConnections = Consts.DEFAULT_MAX_CONNECTIONS;
    /**
     * 连接池中空闲 HTTP 连接时间超过此参数的设定值，则关闭 HTTP 连接，单位：毫秒，默认 60000 毫秒
     */
    private int idleConnectionTimeMills = Consts.DEFAULT_IDLE_CONNECTION_TIME_MILLS;
    /**
     * 建立连接超时时间，单位：毫秒，默认 10000 毫秒
     */
    private int connectTimeoutMills = Consts.DEFAULT_CONNECT_TIMEOUT_MILLS;

    /**
     * Socket 读超时时间，单位：毫秒，默认 30000 毫秒
     */
    private int readTimeoutMills = Consts.DEFAULT_READ_TIMEOUT_MILLS;
    /**
     * Socket 写超时时间，单位：毫秒，默认 30000 毫秒
     */
    private int writeTimeoutMills = Consts.DEFAULT_WRITE_TIMEOUT_MILLS;

    /**
     * 代理服务器的主机地址，当前只支持 http 协议
     */
    private String proxyHost;
    /**
     * 代理服务器的端口号
     */
    private int proxyPort;
    /**
     * 连接代理服务器时使用的用户名
     */
    private String proxyUserName;
    /**
     * 连接代理服务器时使用的用户密码
     */
    private String proxyPassword;

    /**
     * 是否开启 SSL 证书校验，默认为 true
     */
    private boolean enableVerifySSL = Consts.DEFAULT_ENABLE_VERIFY_SSL;

    /**
     * DNS 缓存的有效期，单位：毫秒，小与等于 0 时代表关闭 DNS 缓存，默认为 0
     */
    @Deprecated
    private int dnsCacheTimeMills;

    /**
     * DNS 缓存的有效期，单位：分钟，小与等于 0 时代表关闭 DNS 缓存，默认为 0
     */
    private int dnsCacheTimeMinutes;

    /**
     * 最大重试次数，默认为 3 次
     */
    private int maxRetryCount = Consts.DEFAULT_MAX_RETRY_COUNT;

    private int except100ContinueThreshold = Consts.DEFAULT_EXPECT_100_CONTINUE_THRESHOLD;

    private boolean isHttp;

    private int highLatencyLogThreshold = Consts.DEFAULT_HIGH_LATENCY_LOG_THRESHOLD;

    public int getMaxConnections() {
        return maxConnections;
    }

    public TransportConfig setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public int getIdleConnectionTimeMills() {
        return idleConnectionTimeMills;
    }

    public TransportConfig setIdleConnectionTimeMills(int idleConnectionTimeMills) {
        this.idleConnectionTimeMills = idleConnectionTimeMills;
        return this;
    }

    public int getConnectTimeoutMills() {
        return connectTimeoutMills;
    }

    public TransportConfig setConnectTimeoutMills(int connectTimeoutMills) {
        this.connectTimeoutMills = connectTimeoutMills;
        return this;
    }

    public int getReadTimeoutMills() {
        return readTimeoutMills;
    }

    public TransportConfig setReadTimeoutMills(int readTimeoutMills) {
        this.readTimeoutMills = readTimeoutMills;
        return this;
    }

    public int getWriteTimeoutMills() {
        return writeTimeoutMills;
    }

    public TransportConfig setWriteTimeoutMills(int writeTimeoutMills) {
        this.writeTimeoutMills = writeTimeoutMills;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public TransportConfig setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public TransportConfig setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public TransportConfig setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
        return this;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public TransportConfig setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public boolean isEnableVerifySSL() {
        return enableVerifySSL;
    }

    public TransportConfig setEnableVerifySSL(boolean enableVerifySSL) {
        this.enableVerifySSL = enableVerifySSL;
        return this;
    }

    @Deprecated
    public int getDnsCacheTimeMills() {
        return dnsCacheTimeMills;
    }

    @Deprecated
    public TransportConfig setDnsCacheTimeMills(int dnsCacheTimeMills) {
        this.dnsCacheTimeMills = dnsCacheTimeMills;
        return this;
    }

    public int getDnsCacheTimeMinutes() {
        return dnsCacheTimeMinutes;
    }

    public TransportConfig setDnsCacheTimeMinutes(int dnsCacheTimeMinutes) {
        this.dnsCacheTimeMinutes = dnsCacheTimeMinutes;
        return this;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public TransportConfig setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    public int getExcept100ContinueThreshold() {
        return except100ContinueThreshold;
    }

    public TransportConfig setExcept100ContinueThreshold(int except100ContinueThreshold) {
        this.except100ContinueThreshold = except100ContinueThreshold;
        return this;
    }

    public boolean isHttp() {
        return isHttp;
    }

    public TransportConfig setHttp(boolean http) {
        isHttp = http;
        return this;
    }

    public int getHighLatencyLogThreshold() {
        return highLatencyLogThreshold;
    }

    public TransportConfig setHighLatencyLogThreshold(int highLatencyLogThreshold) {
        this.highLatencyLogThreshold = highLatencyLogThreshold;
        return this;
    }

    public static TransportConfigBuilder builder() {
        return new TransportConfigBuilder();
    }

    public static final class TransportConfigBuilder {
        private int maxConnections = Consts.DEFAULT_MAX_CONNECTIONS;
        private int idleConnectionTimeMills = Consts.DEFAULT_IDLE_CONNECTION_TIME_MILLS;
        private int connectTimeoutMills = Consts.DEFAULT_CONNECT_TIMEOUT_MILLS;
        private int readTimeoutMills = Consts.DEFAULT_READ_TIMEOUT_MILLS;
        private int writeTimeoutMills = Consts.DEFAULT_WRITE_TIMEOUT_MILLS;
        private String proxyHost;
        private int proxyPort;
        private String proxyUserName;
        private String proxyPassword;
        private boolean enableVerifySSL = Consts.DEFAULT_ENABLE_VERIFY_SSL;
        @Deprecated
        private int dnsCacheTimeMills;
        private int dnsCacheTimeMinutes;
        private int maxRetryCount = Consts.DEFAULT_MAX_RETRY_COUNT;

        private int except100ContinueThreshold = Consts.DEFAULT_EXPECT_100_CONTINUE_THRESHOLD;

        private int highLatencyLogThreshold = Consts.DEFAULT_HIGH_LATENCY_LOG_THRESHOLD;

        private TransportConfigBuilder() {
        }

        public TransportConfigBuilder maxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public TransportConfigBuilder idleConnectionTimeMills(int idleConnectionTimeMills) {
            this.idleConnectionTimeMills = idleConnectionTimeMills;
            return this;
        }

        public TransportConfigBuilder connectTimeoutMills(int connectTimeoutMills) {
            this.connectTimeoutMills = connectTimeoutMills;
            return this;
        }

        public TransportConfigBuilder readTimeoutMills(int readTimeoutMills) {
            this.readTimeoutMills = readTimeoutMills;
            return this;
        }

        public TransportConfigBuilder writeTimeoutMills(int writeTimeoutMills) {
            this.writeTimeoutMills = writeTimeoutMills;
            return this;
        }

        public TransportConfigBuilder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public TransportConfigBuilder proxyPort(int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public TransportConfigBuilder proxyUserName(String proxyUserName) {
            this.proxyUserName = proxyUserName;
            return this;
        }

        public TransportConfigBuilder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public TransportConfigBuilder enableVerifySSL(boolean enableVerifySSL) {
            this.enableVerifySSL = enableVerifySSL;
            return this;
        }

        @Deprecated
        public TransportConfigBuilder dnsCacheTimeMills(int dnsCacheTimeMills) {
            this.dnsCacheTimeMills = dnsCacheTimeMills;
            return this;
        }

        public TransportConfigBuilder dnsCacheTimeMinutes(int dnsCacheTimeMinutes) {
            this.dnsCacheTimeMinutes = dnsCacheTimeMinutes;
            return this;
        }

        public TransportConfigBuilder maxRetryCount(int maxRetryCounts) {
            this.maxRetryCount = maxRetryCounts;
            return this;
        }

        public TransportConfigBuilder except100ContinueThreshold(int except100ContinueThreshold) {
            this.except100ContinueThreshold = except100ContinueThreshold;
            return this;
        }

        public TransportConfigBuilder highLatencyLogThreshold(int highLatencyLogThreshold) {
            this.highLatencyLogThreshold = highLatencyLogThreshold;
            return this;
        }

        public TransportConfig build() {
            TransportConfig transportConfig = new TransportConfig();
            transportConfig.setMaxConnections(maxConnections);
            transportConfig.setIdleConnectionTimeMills(idleConnectionTimeMills);
            transportConfig.setConnectTimeoutMills(connectTimeoutMills);
            transportConfig.setReadTimeoutMills(readTimeoutMills);
            transportConfig.setWriteTimeoutMills(writeTimeoutMills);
            transportConfig.setProxyHost(proxyHost);
            transportConfig.setProxyPort(proxyPort);
            transportConfig.setProxyUserName(proxyUserName);
            transportConfig.setProxyPassword(proxyPassword);
            transportConfig.setEnableVerifySSL(enableVerifySSL);
            transportConfig.setDnsCacheTimeMinutes(dnsCacheTimeMinutes);
            transportConfig.setMaxRetryCount(maxRetryCount);
            transportConfig.setExcept100ContinueThreshold(except100ContinueThreshold);
            transportConfig.setHighLatencyLogThreshold(highLatencyLogThreshold);
            return transportConfig;
        }
    }

    @Override
    public String toString() {
        return "TransportConfig{" +
                "maxConnections=" + maxConnections +
                ", idleConnectionTimeMills=" + idleConnectionTimeMills +
                ", connectTimeoutMills=" + connectTimeoutMills +
                ", readTimeoutMills=" + readTimeoutMills +
                ", writeTimeoutMills=" + writeTimeoutMills +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUserName='" + proxyUserName + '\'' +
                ", proxyPassword='" + proxyPassword + '\'' +
                ", enableVerifySSL=" + enableVerifySSL +
                ", dnsCacheTimeMinutes=" + dnsCacheTimeMinutes +
                ", maxRetryCount=" + maxRetryCount +
                ", except100ContinueThreshold=" + except100ContinueThreshold +
                '}';
    }

    @Deprecated
    private int keepAlive;
    @Deprecated
    private int maxIdleCount;
    @Deprecated
    private int tlsHandshakeTimeout;
    @Deprecated
    private int responseHeaderTimeout;
    @Deprecated
    private int expectContinueTimeout;
    @Deprecated
    private int dialTimeout;
    @Deprecated
    private int requestTimeout;
    @Deprecated
    private int readTimeout;
    @Deprecated
    private int writeTimeout;
    @Deprecated
    private int idleConnectionTime;
    @Deprecated
    private int connectTimeout;

    @Deprecated
    public TransportConfig defaultTransportConfig(){
        this.maxIdleCount = 1024;
        this.dialTimeout = 10;
        this.keepAlive = 60;
        this.connectTimeout = 10;
        this.tlsHandshakeTimeout = 10;
        this.responseHeaderTimeout = 60;
        this.expectContinueTimeout = 3;
        this.readTimeout = 30;
        this.writeTimeout = 30;
        return this;
    }

    @Deprecated
    public int getMaxIdleCount() {
        return maxConnections;
    }

    @Deprecated
    public TransportConfig setMaxIdleCount(int maxIdleCount) {
        this.maxConnections = maxIdleCount;
        return this;
    }

    @Deprecated
    public TransportConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    @Deprecated
    public int getRequestTimeout() { return requestTimeout; }

    @Deprecated
    public int getDialTimeout() {
        return dialTimeout;
    }

    @Deprecated
    public TransportConfig setDialTimeout(int dialTimeout) {
        this.dialTimeout = dialTimeout;
        return this;
    }

    @Deprecated
    public int getKeepAlive() {
        return keepAlive;
    }

    @Deprecated
    public TransportConfig setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    @Deprecated
    public int getTlsHandshakeTimeout() {
        return tlsHandshakeTimeout;
    }

    @Deprecated
    public TransportConfig setTlsHandshakeTimeout(int tlsHandshakeTimeout) {
        this.tlsHandshakeTimeout = tlsHandshakeTimeout;
        return this;
    }

    @Deprecated
    public int getResponseHeaderTimeout() {
        return responseHeaderTimeout;
    }

    @Deprecated
    public TransportConfig setResponseHeaderTimeout(int responseHeaderTimeout) {
        this.responseHeaderTimeout = responseHeaderTimeout;
        return this;
    }

    @Deprecated
    public int getExpectContinueTimeout() {
        return expectContinueTimeout;
    }

    @Deprecated
    public TransportConfig setExpectContinueTimeout(int expectContinueTimeout) {
        this.expectContinueTimeout = expectContinueTimeout;
        return this;
    }

    @Deprecated
    public int getReadTimeout() {
        return readTimeout;
    }

    @Deprecated
    public TransportConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Deprecated
    public int getWriteTimeout() {
        return writeTimeout;
    }

    @Deprecated
    public TransportConfig setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    @Deprecated
    public int getIdleConnectionTime() {
        return idleConnectionTime;
    }

    @Deprecated
    public TransportConfig setIdleConnectionTime(int idleConnectionTime) {
        this.idleConnectionTime = idleConnectionTime;
        return this;
    }

    @Deprecated
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Deprecated
    public TransportConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
}
