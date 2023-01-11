package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class ClientInstance {
    private static final TransportConfig config = TransportConfig.builder().dnsCacheTimeMinutes(1).build();
    private static Transport transport;
    private static Signer signer;
    private static TosRequestFactory factory;
    private static TosBucketRequestHandler bucketHandler = null;
    private static TosObjectRequestHandler objectHandler = null;
    private static TosFileRequestHandler fileHandler = null;
    private static TosPreSignedRequestHandler preSignedRequestHandler = null;

    public static TosBucketRequestHandler getBucketRequestHandlerInstance() {
        if (bucketHandler != null) {
            return bucketHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            bucketHandler = new TosBucketRequestHandler(transport, factory);
        }
        return bucketHandler;
    }

    public static TosObjectRequestHandler getObjectRequestHandlerInstance() {
        if (objectHandler != null) {
            return objectHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            objectHandler = new TosObjectRequestHandler(transport, factory).setEnableCrcCheck(true);
        }
        return objectHandler;
    }

    public static TosFileRequestHandler getFileRequestHandlerInstance() {
        if (fileHandler != null) {
            return fileHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            fileHandler = new TosFileRequestHandler(getObjectRequestHandlerInstance(), transport, factory)
                    .setEnableCrcCheck(true);
        }
        return fileHandler;
    }

    public static TosPreSignedRequestHandler getPreSignedRequestHandlerInstance() {
        if (preSignedRequestHandler != null) {
            return preSignedRequestHandler;
        }
        synchronized (ClientInstance.class) {
            init();
            Credentials credentials = new StaticCredentials(Consts.accessKey, Consts.secretKey);
            preSignedRequestHandler = new TosPreSignedRequestHandler(factory, signer);
        }
        return preSignedRequestHandler;
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
            factory = new TosRequestFactory(signer, Consts.endpoint);
        }
    }
}
