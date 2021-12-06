package com.volcengine.tos.session;

import com.volcengine.tos.transport.DefaultTransport;
import com.volcengine.tos.transport.TransportConfig;

public class SessionTransport extends DefaultTransport {
    private static SessionTransport sessionTransport;
    private static TransportConfig sessionTransportConfig;
    public SessionTransport(TransportConfig config) {
        super(config);
        sessionTransportConfig = config;
    }

    public synchronized static SessionTransport getInstance(TransportConfig config){
        sessionTransport = new SessionTransport(config);
        return sessionTransport;
    }

    public synchronized static SessionTransport getInstance(){
        return sessionTransport;
    }

    public synchronized static TransportConfig getConfigInstance(){
        return sessionTransportConfig;
    }
}
