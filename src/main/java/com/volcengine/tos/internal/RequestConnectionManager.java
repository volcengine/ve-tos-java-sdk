package com.volcengine.tos.internal;

import java.io.IOException;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.ConnectionEndpoint;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.io.LeaseRequest;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class RequestConnectionManager implements HttpClientConnectionManager {

    private final PoolingHttpClientConnectionManager connectionManager;

    public RequestConnectionManager(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void connect(final ConnectionEndpoint endpoint, final TimeValue timeout, final HttpContext context) throws IOException {
        context.setAttribute("connection_start", System.currentTimeMillis());
        this.connectionManager.connect(endpoint, timeout, context);
        context.setAttribute("connection_end", System.currentTimeMillis());
        context.setAttribute("dns_start", RequestDnsTimeHolder.getStart());
        context.setAttribute("dns_end", RequestDnsTimeHolder.getEnd());
        RequestDnsTimeHolder.clear();
    }

    @Override
    public void close(CloseMode closeMode) {
        this.connectionManager.close(closeMode);
    }

    @Override
    public void close() throws IOException {
        this.connectionManager.close();
    }

    @Override
    public LeaseRequest lease(String id, HttpRoute route, Timeout requestTimeout, Object state) {
        return this.connectionManager.lease(id, route, requestTimeout, state);
    }

    @Override
    public void release(ConnectionEndpoint endpoint, Object newState, TimeValue validDuration) {
        this.connectionManager.release(endpoint, newState, validDuration);
    }

    @Override
    public void upgrade(ConnectionEndpoint endpoint, HttpContext context) throws IOException {
        this.connectionManager.upgrade(endpoint, context);
    }
}
