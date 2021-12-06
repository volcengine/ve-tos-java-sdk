package com.volcengine.tos.transport;


public class TransportConfig {
    private int maxIdleCount;
    private int requestTimeout;
    private int dialTimeout;
    private int keepAlive;
    private int connectTimeout;
    private int tlsHandshakeTimeout;
    private int responseHeaderTimeout;
    private int expectContinueTimeout;

    private int readTimeout;
    private int writeTimeout;

    public TransportConfig defaultTransportConfig(){
        this.maxIdleCount = 128;
        this.dialTimeout = 10;
        this.keepAlive = 30;
        this.connectTimeout = 60;
        this.tlsHandshakeTimeout = 10;
        this.responseHeaderTimeout = 60;
        this.expectContinueTimeout = 3;
        this.readTimeout = 120;
        this.writeTimeout = 120;
        return this;
    }

    public int getMaxIdleCount() {
        return maxIdleCount;
    }

    public TransportConfig setMaxIdleCount(int maxIdleCount) {
        this.maxIdleCount = maxIdleCount;
        return this;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public TransportConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public int getDialTimeout() {
        return dialTimeout;
    }

    public TransportConfig setDialTimeout(int dialTimeout) {
        this.dialTimeout = dialTimeout;
        return this;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public TransportConfig setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public TransportConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getTlsHandshakeTimeout() {
        return tlsHandshakeTimeout;
    }

    public TransportConfig setTlsHandshakeTimeout(int tlsHandshakeTimeout) {
        this.tlsHandshakeTimeout = tlsHandshakeTimeout;
        return this;
    }

    public int getResponseHeaderTimeout() {
        return responseHeaderTimeout;
    }

    public TransportConfig setResponseHeaderTimeout(int responseHeaderTimeout) {
        this.responseHeaderTimeout = responseHeaderTimeout;
        return this;
    }

    public int getExpectContinueTimeout() {
        return expectContinueTimeout;
    }

    public TransportConfig setExpectContinueTimeout(int expectContinueTimeout) {
        this.expectContinueTimeout = expectContinueTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public TransportConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public TransportConfig setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    @Override
    public String toString() {
        return "TransportConfig{" +
                "maxIdleCount=" + maxIdleCount +
                ", requestTimeout=" + requestTimeout +
                ", dialTimeout=" + dialTimeout +
                ", keepAlive=" + keepAlive +
                ", connectTimeout=" + connectTimeout +
                ", tlsHandshakeTimeout=" + tlsHandshakeTimeout +
                ", responseHeaderTimeout=" + responseHeaderTimeout +
                ", expectContinueTimeout=" + expectContinueTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                '}';
    }
}
