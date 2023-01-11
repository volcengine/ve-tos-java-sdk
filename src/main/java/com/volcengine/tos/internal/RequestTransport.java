package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.io.TosRepeatableInputStream;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.model.SimpleDataTransferListenInputStream;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.dnscache.DefaultDnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import com.volcengine.tos.internal.util.ratelimit.RateLimitedInputStream;
import com.volcengine.tos.model.object.TosObjectInputStream;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.*;
import okhttp3.Authenticator;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CheckedInputStream;

/**
 * @author volcengine
 * 1. Basic HTTP request and response handler
 * 2. Retrier in exception
 * 3. Custom HTTP client config
 * 4. DNS cache
 * 5. Enable/Disable verify SSL certification
 * 6. HTTP proxy
 * 7. Rate limiter
 * 8. ...
 */
public class RequestTransport implements Transport {
    private static final MediaType DEFAULT_MEDIA_TYPE = null;
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    private final OkHttpClient client;
    private static final Logger LOG = LoggerFactory.getLogger(RequestTransport.class);
    private int maxRetries;
    private DnsCacheService dnsCacheService;

    public RequestTransport(TransportConfig config){
        ConnectionPool connectionPool = new ConnectionPool(config.getMaxConnections(),
                config.getIdleConnectionTimeMills(), TimeUnit.MILLISECONDS);

        Dispatcher dispatcher = new Dispatcher();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (!config.isEnableVerifySSL()) {
            builder = ignoreCertificate(builder);
        }
        if (StringUtils.isNotEmpty(config.getProxyHost()) && config.getProxyPort() > 0 ) {
            SocketAddress socketAddress = new InetSocketAddress(config.getProxyHost(), config.getProxyPort());
            builder.proxy(new Proxy(Proxy.Type.HTTP, socketAddress));
            if (StringUtils.isNotEmpty(config.getProxyUserName())) {
                Authenticator proxyAuthenticator = (route, response) -> {
                    String credential = Credentials.basic(config.getProxyUserName(), config.getProxyPassword());
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential).build();
                };
                builder.proxyAuthenticator(proxyAuthenticator);
            }
        }

        RequestEventListener.RequestEventListenerFactory eventListener = new RequestEventListener.RequestEventListenerFactory(LOG);
        if (config.getDnsCacheTimeMinutes() > 0) {
            dnsCacheService = new DefaultDnsCacheService(config.getDnsCacheTimeMinutes());
            eventListener.setDnsCacheService(dnsCacheService);
            builder.dns(createDnsWithCache());
        }

        this.maxRetries = config.getMaxRetryCount();
        if (maxRetries == 0) {
            maxRetries = DEFAULT_MAX_RETRY_COUNT;
        }
        if (maxRetries < 0) {
            maxRetries = 0;
        }
        this.client = builder.dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .readTimeout(config.getReadTimeoutMills(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeoutMills(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectTimeoutMills(), TimeUnit.MILLISECONDS)
                // 默认关闭重定向
                .followRedirects(false)
                .followSslRedirects(false)
                .eventListenerFactory(eventListener)
                .build();
    }

    private Dns createDnsWithCache() {
        return new Dns() {
            @Override
            public List<InetAddress> lookup(String host) throws UnknownHostException {
                if (host != null && dnsCacheService != null) {
                    List<InetAddress> cache = dnsCacheService.getIpList(host);
                    if (cache != null && cache.size() > 0) {
                        return cache;
                    }
                }
                try {
                    return Arrays.asList(InetAddress.getAllByName(host));
                } catch (NullPointerException e) {
                    UnknownHostException unknownHostException =
                            new UnknownHostException("Broken system behaviour for dns lookup of " + host);
                    unknownHostException.initCause(e);
                    throw unknownHostException;
                }
            }
        };
    }

    @Override
    public void switchConfig(TransportConfig config) {
        // 先只支持重试次数修改
        maxRetries = config.getMaxRetryCount();
        if (maxRetries < 0) {
            maxRetries = 0;
        }
    }

    public OkHttpClient getClient() {
        return client;
    }

    @Override
    public TosResponse roundTrip(TosRequest tosRequest) throws IOException {
        Response response = null;
        long start = System.currentTimeMillis();
        int reqTimes = 1;
        for (int i = 0; i < maxRetries+1; i++, reqTimes++) {
            try{
                Request request = buildRequest(tosRequest);
                response = client.newCall(request).execute();
                if (response.code() >= HttpStatus.INTERNAL_SERVER_ERROR
                || response.code() == HttpStatus.TOO_MANY_REQUESTS) {
                    if (tosRequest.isRetryableOnServerException()) {
                        if (i != maxRetries) {
                            // last time does not need to sleep
                            Thread.sleep(TosUtils.backoff(i));
                            // close response body before retry
                            response.close();
                            if (tosRequest.getContent() != null) {
                                tosRequest.getContent().reset();
                            }
                            continue;
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                LOG.debug("tos: request interrupted while sleeping in retry");
                printAccessLogFailed(e);
                throw new TosClientException("tos: request interrupted", e);
            } catch (IOException e) {
                if (e instanceof SocketException || e instanceof SocketTimeoutException
                        || e instanceof UnknownHostException || e instanceof SSLException) {
                    if (tosRequest.isRetryableOnClientException()) {
                        try{
                            if (i == maxRetries) {
                                // last time does not need to sleep
                                printAccessLogFailed(e);
                                throw e;
                            }
                            Thread.sleep(TosUtils.backoff(i));
                            if (response != null) {
                                response.close();
                            }
                            if (tosRequest.getContent() != null) {
                                tosRequest.getContent().reset();
                            }
                            continue;
                        } catch (InterruptedException ie) {
                            LOG.debug("tos: request interrupted while sleeping in retry");
                            printAccessLogFailed(e);
                            throw new TosClientException("tos: request interrupted", e);
                        }
                    }
                }
                printAccessLogFailed(e);
                throw e;
            }
        }
        long end = System.currentTimeMillis();
        ParamsChecker.ensureNotNull(response, "okhttp response");
        printAccessLogSucceed(response.code(), response.header(TosHeader.HEADER_REQUEST_ID), end-start, reqTimes);
        checkCrc(tosRequest, response);
        InputStream inputStream = response.body() == null ? null : response.body().byteStream();
        return new TosResponse().setStatusCode(response.code())
                .setContentLength(getSize(response))
                .setHeaders(getHeaders(response))
                .setInputStream(inputStream);
    }

    private void printAccessLogSucceed(int code, String reqId, long cost, int reqTimes) {
        LOG.info("tos: status code:{}, request id:{}, request cost {} ms, request {} times\n", code, reqId, cost, reqTimes);
    }

    private void printAccessLogFailed(Exception e) {
        LOG.info("tos: request exception: {}\n", e.toString());
    }

    private void checkCrc(TosRequest tosRequest, Response response) {
        if (tosRequest.isEnableCrcCheck() && response.code() < HttpStatus.MULTIPLE_CHOICE
            && tosRequest.getContent() instanceof CheckedInputStream) {
            // request successful, check crc64
            long clientCrcLong = ((CheckedInputStream) tosRequest.getContent()).getChecksum().getValue();
            String clientHashCrc64Ecma = CRC64Utils.longToUnsignedLongString(clientCrcLong);
            String serverHashCrc64Ecma = response.header(TosHeader.HEADER_CRC64);
            if (!StringUtils.equals(clientHashCrc64Ecma, serverHashCrc64Ecma)) {
                throw new TosClientException("tos: crc64 check failed, expected:" + serverHashCrc64Ecma
                        + ", in fact:" + clientHashCrc64Ecma, null);
            }
        }
    }

    private Request buildRequest(TosRequest request) throws IOException {
        HttpUrl url = request.toURL();
        Request.Builder builder = new Request.Builder().url(url);
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(builder::header);
        }
        wrapInputStream(request);

        switch (request.getMethod() == null ? "" : request.getMethod().toUpperCase()) {
            case HttpMethod.GET:
                builder.get();
                break;
            case HttpMethod.POST:
                if (request.getContent() != null && request.getContentLength() <= 0) {
                    // 兼容 ClientV1 旧接口，有bug，ClientV2 新接口不会走到这里
                    byte[] data = new byte[request.getContent().available()];
                    // Warning 当输入流是网络IO时，这里可能会出错
                    int exact = request.getContent().read(data);
                    if (exact != data.length) {
                        throw new IOException("expected "+data.length+" bytes, but got "+exact+" bytes.");
                    }
                    builder.post(RequestBody.create(getMediaType(request), data));
                } else if (request.getContent() != null){
                    // only appendObject use, not support chunk
                    // make sure the content length is set
                    builder.post(new WrappedTransportRequestBody(getMediaType(request), request.getContent(), request.getContentLength()));
                } else {
                    builder.post(RequestBody.create(getMediaType(request), request.getData()));
                }
                break;
            case HttpMethod.PUT: {
                if (request.getContent() != null) {
                    builder.put(new WrappedTransportRequestBody(getMediaType(request), request.getContent(), request.getContentLength()));
                } else {
                    builder.put(RequestBody.create(getMediaType(request), request.getData()));
                }
                break;
            }
            case HttpMethod.HEAD:
                builder.head();
                break;
            case HttpMethod.DELETE:
                builder.delete();
                break;
            default:
                throw new TosClientException("Method is not supported: " + request.getMethod(), null);
        }
        return builder.build();
    }

    private void wrapInputStream(TosRequest request) {
        InputStream wrappedInputStream = request.getContent();
        if (request.getRateLimiter() != null && request.getContent() != null) {
            wrappedInputStream = new RateLimitedInputStream(wrappedInputStream, request.getRateLimiter());
        }
        if (request.getDataTransferListener() != null && request.getContent() != null) {
            wrappedInputStream = new SimpleDataTransferListenInputStream(request.getContent(),
                    request.getDataTransferListener(), request.getContentLength());
        }
        if (request.isEnableCrcCheck() && request.getContent() != null) {
            CRC64Checksum checksum = new CRC64Checksum(request.getCrc64InitValue());
            wrappedInputStream = new CheckedInputStream(wrappedInputStream, checksum);
        }
        request.setContent(wrappedInputStream);
    }


    private OkHttpClient.Builder ignoreCertificate(OkHttpClient.Builder builder) throws TosClientException {
        LOG.warn("tos: ignore ssl certificate verification");
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOG.warn("tos: exception occurred while configuring ignoreSslCertificate");
            throw new TosClientException("tos: set ignoreCertificate failed", e);
        }
        return builder;
    }

    private MediaType getMediaType(TosRequest request) {
        String type = "";
        if (request.getHeaders() != null) {
            type = request.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        }
        return StringUtils.isEmpty(type) ? DEFAULT_MEDIA_TYPE : MediaType.parse(type);
    }

    private long getSize(Response response) {
        String size = response.header(TosHeader.HEADER_CONTENT_LENGTH);
        if (StringUtils.isEmpty(size)) {
            return 0;
        }
        return Long.parseLong(size);
    }

    private Map<String, String> getHeaders(Response response) {
        Map<String, String> headers = new HashMap<>(response.headers().size());
        for (String name : response.headers().names()) {
            headers.put(name.toLowerCase(), response.header(name));
        }
        return headers;
    }
}

class WrappedTransportRequestBody extends RequestBody implements Closeable {
    private InputStream content;
    private final MediaType contentType;
    private long contentLength;

    WrappedTransportRequestBody(MediaType contentType, InputStream inputStream, long contentLength) {
        ParamsChecker.ensureNotNull(inputStream, "inputStream");
        this.contentType = contentType;
        this.contentLength = contentLength;
        if (this.contentLength < -1L) {
            // chunked
            this.contentLength = -1L;
        }
        this.content = inputStream;
        if (!(this.content instanceof TosObjectInputStream)) {
            // 继承自 TosObjectInputStream 的 inputStream，已经实现 repeatable 特性
            this.content = new TosRepeatableInputStream(inputStream, Consts.DEFAULT_READ_BUFFER_SIZE);
        }
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        // add time cost log
        Source source = null;
        try {
            source = Okio.source(content);
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
        }
    }

    protected InputStream getContent() {
        return this.content;
    }
}