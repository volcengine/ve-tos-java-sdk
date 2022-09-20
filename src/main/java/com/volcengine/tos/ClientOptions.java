package com.volcengine.tos;

import com.volcengine.tos.transport.TransportConfig;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.Transport;

@Deprecated
public class ClientOptions {
    public static ClientOptionsBuilder withCredentials(Credentials credentials){
        return client -> client.setCredentials(credentials);
    }

    public static ClientOptionsBuilder withTransport(Transport transport){
        return client -> client.setTransport(transport);
    }

    public static ClientOptionsBuilder withTransportConfig(TransportConfig config){
        return client -> client.getConfig().setTransportConfig(config);
    }

    public static ClientOptionsBuilder withReadWriteTimeout(int readTimeout, int writeTimeout) {
        return client -> {
            client.getConfig().getTransportConfig().setReadTimeout(readTimeout);
            client.getConfig().getTransportConfig().setWriteTimeout(writeTimeout);
        };
    }

    public static ClientOptionsBuilder withRegion(String region) {
        return client -> client.getConfig().setRegion(region);
    }

    public static ClientOptionsBuilder withSigner(Signer signer) {
        return client -> client.setSigner(signer);
    }

}
