package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class ClientInstance {
    private static final TransportConfig config = TransportConfig.builder().build();
    private static Transport transport;
    private static Signer signer;
    private static TosRequestFactory factory;
    private static TosBucketRequestHandler bucketHandler = null;
    private static TosObjectRequestHandler objectHandler = null;

    public static TosBucketRequestHandler getBucketRequestHandlerInstance() {
        if (bucketHandler != null) {
            return bucketHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            bucketHandler = new TosBucketRequestHandlerImpl(transport, factory);
        }
        return bucketHandler;
    }

    public static TosObjectRequestHandler getObjectRequestHandlerInstance() {
        if (objectHandler != null) {
            return objectHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            objectHandler = new TosObjectRequestHandlerImpl(transport, factory);
        }
        return objectHandler;
    }

    private static void init() {
        if (transport == null) {
            transport = new RequestTransport(config);
        }
        if (signer == null) {
            Credentials credentials = new StaticCredentials(Consts.accessKey, Consts.secretKey);
            signer = new SignV4(credentials, Consts.region);
        }
        if (factory == null) {
            factory = new TosRequestFactoryImpl(signer, Consts.endpoint);
        }
    }
}
