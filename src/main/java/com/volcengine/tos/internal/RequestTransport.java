package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.model.SimpleDataTransferListenInputStream;
import com.volcengine.tos.internal.model.TosCheckedInputStream;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.dnscache.DefaultDnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import com.volcengine.tos.internal.util.ratelimit.RateLimitedInputStream;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.Authenticator;
import okhttp3.*;
import okio.BufferedSink;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
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
    private final OkHttpClient client;
    private int maxRetries;
    private DnsCacheService dnsCacheService;

    public RequestTransport(TransportConfig config){
        ParamsChecker.ensureNotNull(config, "TransportConfig");
        int maxConnections = config.getMaxConnections() > 0 ? config.getMaxConnections() : Consts.DEFAULT_MAX_CONNECTIONS;
        int maxIdleConnectionTimeMills = config.getIdleConnectionTimeMills() > 0 ?
                config.getIdleConnectionTimeMills() : Consts.DEFAULT_IDLE_CONNECTION_TIME_MILLS;
        int readTimeout = config.getReadTimeoutMills() > 0 ? config.getReadTimeoutMills() : Consts.DEFAULT_READ_TIMEOUT_MILLS;
        int writeTimeout = config.getWriteTimeoutMills() > 0 ? config.getWriteTimeoutMills() : Consts.DEFAULT_WRITE_TIMEOUT_MILLS;
        int connectTimeout = config.getConnectTimeoutMills() > 0 ? config.getConnectTimeoutMills() : Consts.DEFAULT_CONNECT_TIMEOUT_MILLS;

        this.maxRetries = config.getMaxRetryCount();
        if (maxRetries < 0) {
            maxRetries = 0;
        }

        ConnectionPool connectionPool = new ConnectionPool(maxConnections,
                maxIdleConnectionTimeMills, TimeUnit.MILLISECONDS);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxConnections);
        dispatcher.setMaxRequestsPerHost(maxConnections);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (!config.isHttp() && !config.isEnableVerifySSL()) {
            // the sdk verifies ssl cert while using https,
            // but if you disable ssl verification,
            // will ignore it by the following method.
            builder = ignoreCertificate(builder);
        }
        if (StringUtils.isNotEmpty(config.getProxyHost()) && config.getProxyPort() > 0 ) {
            addProxyConfig(config, builder);
        }

        RequestEventListener.RequestEventListenerFactory eventListener = new
                RequestEventListener.RequestEventListenerFactory(TosUtils.getLogger());
        if (config.getDnsCacheTimeMinutes() > 0) {
            dnsCacheService = new DefaultDnsCacheService(config.getDnsCacheTimeMinutes());
            eventListener.setDnsCacheService(dnsCacheService);
            builder.dns(createDnsWithCache());
        }

        this.client = builder.dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(false)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                // 默认关闭重定向
                .followRedirects(false)
                .followSslRedirects(false)
                .eventListenerFactory(eventListener)
                .build();
    }

    private void addProxyConfig(TransportConfig config, OkHttpClient.Builder builder) {
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
        Request request = buildRequest(tosRequest);
        for (int i = 0; i < maxRetries+1; i++, reqTimes++) {
            try{
                response = client.newCall(request).execute();
                if (response.code() >= HttpStatus.INTERNAL_SERVER_ERROR
                || response.code() == HttpStatus.TOO_MANY_REQUESTS) {
                    // retry on 5xx, 429
                    if (tosRequest.isRetryableOnServerException()) {
                        // the request can be retried.
                        if (i != maxRetries) {
                            // last time does not need to sleep
                            Thread.sleep(TosUtils.backoff(i));
                            // close response body before retry
                            response.close();
                            continue;
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                response.close();
                TosUtils.getLogger().debug("tos: request interrupted while sleeping in retry");
                printAccessLogFailed(e);
                throw new TosClientException("tos: request interrupted", e);
            } catch (IOException e) {
                if (e instanceof SocketException || e instanceof UnknownHostException
                        || e instanceof SSLException || e instanceof InterruptedIOException) {
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
                            continue;
                        } catch (InterruptedException ie) {
                            if (response != null) {
                                response.close();
                            }
                            TosUtils.getLogger().debug("tos: request interrupted while sleeping in retry");
                            printAccessLogFailed(e);
                            throw new TosClientException("tos: request interrupted", e);
                        }
                    }
                }
                if (response != null) {
                    response.close();
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
                .setInputStream(inputStream)
                .setSource(response.body() == null ? null : response.body().source());
    }

    private void printAccessLogSucceed(int code, String reqId, long cost, int reqTimes) {
        TosUtils.getLogger().info("tos: status code:{}, request id:{}, request cost {} ms, request {} times\n",
                code, reqId, cost, reqTimes);
    }

    private void printAccessLogFailed(Exception e) {
        TosUtils.getLogger().info("tos: request exception: {}\n", e.toString());
    }

    private void checkCrc(TosRequest tosRequest, Response response) {
        boolean needCheckCrc = tosRequest.isEnableCrcCheck()
                && response.code() < HttpStatus.MULTIPLE_CHOICE
                && tosRequest.getContent() != null
                && tosRequest.getContent() instanceof CheckedInputStream;
        if (!needCheckCrc) {
            return;
        }
        // request successfully, check crc64
        long clientCrcLong = ((CheckedInputStream) tosRequest.getContent()).getChecksum().getValue();
        String clientHashCrc64Ecma = CRC64Utils.longToUnsignedLongString(clientCrcLong);
        String serverHashCrc64Ecma = response.header(TosHeader.HEADER_CRC64);
        if (!StringUtils.equals(clientHashCrc64Ecma, serverHashCrc64Ecma)) {
            throw new TosClientException("tos: crc64 check failed, " +
                    "expected:" + serverHashCrc64Ecma +
                    ", in fact:" + clientHashCrc64Ecma,
                    null);
        }
    }

    private Request buildRequest(TosRequest request) throws IOException {
        HttpUrl url = request.toURL();
        Request.Builder builder = new Request.Builder().url(url);
        addHeader(request, builder);
        wrapTosRequestContent(request);

        switch (request.getMethod() == null ? "" : request.getMethod().toUpperCase()) {
            case HttpMethod.GET:
                builder.get();
                break;
            case HttpMethod.POST:
                if (request.getContent() != null && request.getContentLength() <= 0) {
                    // 兼容 ClientV1 旧接口，有bug，ClientV2 新接口不会走到这里
                    byte[] data = new byte[request.getContent().available()];
                    int exact = request.getContent().read(data);
                    if (exact != -1 && exact != data.length) {
                        throw new IOException("expected "+data.length+" bytes, but got "+exact+" bytes.");
                    }
                    builder.post(RequestBody.create(getMediaType(request), data));
                } else if (request.getContent() != null){
                    // only appendObject use, not support chunk
                    // make sure the content length is set
                    builder.post(new WrappedTransportRequestBody(
                            getMediaType(request), request.getContent(), request.getContentLength()));
                } else if (request.getData() != null){
                    builder.post(RequestBody.create(getMediaType(request), request.getData()));
                } else {
                    builder.post(RequestBody.create(getMediaType(request), new byte[0]));
                }
                break;
            case HttpMethod.PUT: {
                if (request.getContent() != null) {
                    builder.put(new WrappedTransportRequestBody(
                            getMediaType(request), request.getContent(), request.getContentLength()));
                } else if (request.getData() != null){
                    builder.put(RequestBody.create(getMediaType(request), request.getData()));
                } else {
                    builder.put(RequestBody.create(getMediaType(request), new byte[0]));
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

    private void addHeader(TosRequest request, Request.Builder builder) {
        if (request == null || builder == null || request.getHeaders() == null) {
            return;
        }
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.header(key, value);
        }
    }

    private void wrapTosRequestContent(TosRequest request) {
        if (request == null || request.getContent() == null) {
            return;
        }
        // 确保 TosRequest 拿到的 InputStream 为外部传入，没有封装过，统一在此方法中进行封装
        InputStream originalInputStream = request.getContent();
        InputStream wrappedInputStream = null;
        if (originalInputStream.markSupported()) {
            // 流本身支持 mark&reset，不做封装
            wrappedInputStream = originalInputStream;
        } else {
            if (originalInputStream instanceof FileInputStream) {
                // 文件流封装成可重试的流
                wrappedInputStream = new TosRepeatableFileInputStream((FileInputStream) originalInputStream);
            } else {
                wrappedInputStream = new BufferedInputStream(originalInputStream, Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE);
            }
        }
        if (originalInputStream.markSupported()) {
            int readLimit = Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE;
            if (request.getReadLimit() > 0) {
                readLimit = request.getReadLimit();
            }
            wrappedInputStream.mark(readLimit);
        }
        if (request.getRateLimiter() != null) {
            wrappedInputStream = new RateLimitedInputStream(wrappedInputStream, request.getRateLimiter());
        }
        if (request.getDataTransferListener() != null) {
            wrappedInputStream = new SimpleDataTransferListenInputStream(wrappedInputStream,
                    request.getDataTransferListener(), request.getContentLength());
        }
        if (request.isEnableCrcCheck()) {
            // 此封装需保证放最外层，因为上传后需要对上传结果的 crc 进行校验。
            CRC64Checksum checksum = new CRC64Checksum(request.getCrc64InitValue());
            wrappedInputStream = new TosCheckedInputStream(wrappedInputStream, checksum);
        }
        request.setContent(wrappedInputStream);
    }


    private OkHttpClient.Builder ignoreCertificate(OkHttpClient.Builder builder) throws TosClientException {
        TosUtils.getLogger().info("tos: ignore ssl certificate verification");
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
            throw new TosClientException("tos: set ignoreCertificate failed", e);
        }
        return builder;
    }

    private MediaType getMediaType(TosRequest request) {
        String type = "";
        if (request.getHeaders() != null && request.getHeaders().containsKey(TosHeader.HEADER_CONTENT_TYPE)) {
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
            parseHeader(response, headers, name);
        }
        return headers;
    }

    private void parseHeader(Response response, Map<String, String> headers, String name) {
        // 原始的 key/value 值
        String key = name;
        String value = response.header(name);
        // 在此统一处理 header 的解码
        if (StringUtils.startWithIgnoreCase(key, TosHeader.HEADER_META_PREFIX)) {
            // 对于自定义元数据，对 key/value 包含的中文汉字进行 URL 解码
            key = TosUtils.decodeHeader(key);
            value = TosUtils.decodeHeader(value);
        }
        if (StringUtils.equalsIgnoreCase(key, TosHeader.HEADER_CONTENT_DISPOSITION)) {
            // 对于 Content-Disposition 头，对 value 包含的中文汉字进行 URL 解码
            value = TosUtils.decodeHeader(value);
        }
        headers.put(key.toLowerCase(), value);
    }
}

class WrappedTransportRequestBody extends RequestBody implements Closeable {
    private InputStream content;
    private final MediaType contentType;
    private long contentLength;
    private volatile long totalBytesRead = 0;

    WrappedTransportRequestBody(final MediaType contentType, final InputStream content, final long contentLength) {
        ParamsChecker.ensureNotNull(content, "Content");
        this.content = content;
        this.contentType = contentType;
        this.contentLength = contentLength;
        if (this.contentLength < 0) {
            // chunked
            this.contentLength = -1L;
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
        if (totalBytesRead > 0 && this.content != null && this.content.markSupported()) {
            TosUtils.getLogger().debug("tos: okhttp writeTo call reset");
            this.content.reset();
            totalBytesRead = 0;
        }
        if (this.contentLength < 0) {
            writeAllWithChunked(sink);
        } else {
            writeAll(sink);
        }
    }

    private void writeAll(BufferedSink sink) throws IOException {
        int bytesRead = 0;
        byte[] tmp = new byte[Consts.DEFAULT_READ_BUFFER_SIZE];
        long remaining = this.contentLength;
        while (remaining > 0) {
            int maxToRead = tmp.length < remaining ? tmp.length: (int) remaining;
            bytesRead = this.content.read(tmp, 0, maxToRead);
            if (bytesRead == -1) {
                // eof
                break;
            }
            sink.write(tmp, 0, bytesRead);
            totalBytesRead += bytesRead;
            remaining -= bytesRead;
        }
    }

    private void writeAllWithChunked(BufferedSink sink) throws IOException {
        int bytesRead = 0;
        byte[] tmp = new byte[Consts.DEFAULT_READ_BUFFER_SIZE];
        // chunked
        bytesRead = this.content.read(tmp);
        while (bytesRead != -1) {
            sink.write(tmp, 0, bytesRead);
            totalBytesRead += bytesRead;
            bytesRead = this.content.read(tmp);
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