package com.volcengine.tos.internal;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;

import com.volcengine.tos.comm.TosHeader;

public class RequestLatencyInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

    private final Logger log;

    private int highLatencyLogThreshold;

    public RequestLatencyInterceptor(Logger log) {
        this.log = log;
    }

    public RequestLatencyInterceptor setHighLatencyLogThreshold(int highLatencyLogThreshold) {
        this.highLatencyLogThreshold = highLatencyLogThreshold;
        return this;
    }

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {
        context.setAttribute("call_end", System.currentTimeMillis());
        long callStart = (long) context.getAttribute("call_start");
        long callEnd = (long) context.getAttribute("call_end");
        long dnsStart = 0L;
        long dnsEnd = 0L;
        long connectStart = 0L;
        long connectEnd = 0L;
        if (context.getAttribute("dns_start") != null) {
            dnsStart = (long) context.getAttribute("dns_start");
            dnsEnd = (long) context.getAttribute("dns_end");
        }
        if (context.getAttribute("connection_start") != null) {
            connectStart = (long) context.getAttribute("connection_start");
            connectEnd = (long) context.getAttribute("connection_end");
        }

        long latency = callEnd - callStart;
        long dnsLatency = dnsEnd - dnsStart;
        long connectLatency = connectEnd - connectStart;
        String requestId = null;
        if (response.getFirstHeader(TosHeader.HEADER_REQUEST_ID) != null) {
            requestId = response.getFirstHeader(TosHeader.HEADER_REQUEST_ID).getValue();
        }
        String method = (String) context.getAttribute("method");
        String path = (String) context.getAttribute("path");
        String host = (String) context.getAttribute("host");
        this.log.debug("requestId: {}, method: {}, host: {}, request uri: {}, "
                + "dns cost: {} ms, connect cost: {} ms, "
                + "request cost: {} ms\n",
                requestId, method, host, path,
                dnsLatency, connectLatency, latency);
        if (this.highLatencyLogThreshold > 0 && latency > 500) {
            long body_size = (Long) context.getAttribute("body");
            long dataSize = body_size > 1024 ? body_size : 1024;

            float cost_seconds = (float) latency / 1000;
            if ((float) dataSize / 1024 / cost_seconds < this.highLatencyLogThreshold) {
                this.log.warn("[high latency request] requestId: {}, method: {}, host: {}, request uri: {}, "
                        + "dns cost: {} ms, connect cost: {} ms, "
                        + "request cost: {} ms\n",
                        requestId, method, host, path,
                        dnsLatency, connectLatency, latency);
            }
        }
    }

    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {
        context.setAttribute("call_start", System.currentTimeMillis());
        if (request != null) {
            context.setAttribute("method", request.getMethod());
            if (entity == null) {
                context.setAttribute("body", 0L);
            } else {
                context.setAttribute("body", entity.getContentLength());
            }

            try {
                context.setAttribute("host", request.getUri().getHost());
                context.setAttribute("path", request.getUri().getPath());
            } catch (URISyntaxException e) {
                this.log.debug("Failed to get host and path from request, exception: {}\n", e.toString());
                context.setAttribute("host", "unknown");
                context.setAttribute("path", "unknown");
            }
        }
    }

}
