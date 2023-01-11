package com.volcengine.tos.internal;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import okhttp3.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class RequestEventListener extends EventListener {
    private long callStart;
    private long callEnd;
    //建立连接
    private long dnsStart;
    private long dnsEnd;
    private long connectStart;
    private long connectEnd;
    private long secureConnectStart;
    private long secureConnectEnd;

    //连接已经建立
    private long requestStart;
    private long requestEnd;
    private long responseStart;
    private long responseEnd;

    private String method;
    private String host;
    private String path;
    private String reqId;

    public static class RequestEventListenerFactory implements EventListener.Factory {
        private final Logger log;
        private DnsCacheService dnsCacheService;

        public RequestEventListenerFactory(Logger log) {
            this.log = log;
        }

        public RequestEventListenerFactory setDnsCacheService(DnsCacheService dnsCacheService) {
            this.dnsCacheService = dnsCacheService;
            return this;
        }

        @Override
        public EventListener create(Call call) {
            return new RequestEventListener(log).setDnsCacheService(dnsCacheService);
        }
    }

    private Logger log;
    private DnsCacheService dnsCacheService;

    public RequestEventListener(Logger log) {
        callStart = -1;
        callEnd = -1;

        dnsStart = -1;
        dnsEnd = -1;
        connectStart = -1;
        connectEnd = -1;
        secureConnectStart = -1;
        secureConnectEnd = -1;

        requestStart = -1;
        requestEnd = -1;
        responseStart = -1;
        responseEnd = -1;

        this.log = log;
    }

    public RequestEventListener setDnsCacheService(DnsCacheService dnsCacheService) {
        this.dnsCacheService = dnsCacheService;
        return this;
    }

    @Override
    public void callStart(Call call) {
        callStart = System.currentTimeMillis();
        if (call != null) {
            method = call.request().method();
            host = call.request().url().host();
            path = call.request().url().encodedPath();
        }
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        dnsStart = System.currentTimeMillis();
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        dnsEnd = System.currentTimeMillis();
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        connectStart = System.currentTimeMillis();
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        connectEnd = System.currentTimeMillis();
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        connectEnd = System.currentTimeMillis();
        // remove
        if (this.dnsCacheService != null && inetSocketAddress != null && inetSocketAddress.getAddress() != null
        && inetSocketAddress.getAddress().getHostAddress() != null) {
            this.dnsCacheService.removeAddress(call.request().url().host(), inetSocketAddress.getAddress().getHostAddress());
        }
    }

    @Override
    public void secureConnectStart(Call call) {
        secureConnectStart = System.currentTimeMillis();
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        secureConnectEnd = System.currentTimeMillis();
    }

    @Override
    public void requestHeadersStart(Call call) {
        requestStart = System.currentTimeMillis();
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        requestEnd = System.currentTimeMillis();
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        requestEnd = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersStart(Call call) {
        responseStart = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        responseEnd = System.currentTimeMillis();
        if (response != null) {
            reqId = response.header(TosHeader.HEADER_REQUEST_ID);
        }
    }

    @Override
    public void responseBodyStart(Call call) {
        responseStart = System.currentTimeMillis();
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        responseEnd = System.currentTimeMillis();
    }

    @Override
    public void callEnd(Call call) {
        callEnd = System.currentTimeMillis();
        log.debug("requestId: {}, method: {}, host: {}, request uri: {}, " +
                "dns cost: {} ms, connect cost: {} ms, tls handshake cost: {} ms, " +
                "send headers and body cost: {} ms, wait response cost: {} ms, request cost: {} ms\n",
                reqId, method, host, path,
                (dnsEnd-dnsStart), (connectEnd-connectStart), (secureConnectEnd-secureConnectStart),
                (requestEnd-requestStart), (responseEnd-responseStart), (callEnd-callStart));
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        callEnd = System.currentTimeMillis();
        callEnd = System.currentTimeMillis();
        log.debug("requestId: {}, method: {}, host: {}, request uri: {}, " +
                        "dns cost: {} ms, connect cost: {} ms, tls handshake cost: {} ms, " +
                        "send headers and body cost: {} ms, wait response cost: {} ms, request cost: {} ms\n",
                reqId, method, host, path,
                (dnsEnd-dnsStart), (connectEnd-connectStart), (secureConnectEnd-secureConnectStart),
                (requestEnd-requestStart), (responseEnd-responseStart), (callEnd-callStart));
    }
}
