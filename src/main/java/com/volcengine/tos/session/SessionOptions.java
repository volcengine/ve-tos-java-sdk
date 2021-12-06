package com.volcengine.tos.session;

import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.transport.TransportConfig;

public class SessionOptions {

    public static SessionOptionsBuilder withTransportConfig(TransportConfig config){
        return client -> client.getConfig().setTransportConfig(config);
    }

    public static SessionOptionsBuilder withReadWriteTimeout(int readTimeout, int writeTimeout) {
        return client -> {
            client.getConfig().getTransportConfig().setReadTimeout(readTimeout);
            client.getConfig().getTransportConfig().setWriteTimeout(writeTimeout);
        };
    }

    public static SessionOptionsBuilder withSigner(Signer signer) {
        return client -> client.setSigner(signer);
    }
}
