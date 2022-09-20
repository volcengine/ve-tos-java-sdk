package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.io.TosRepeatableInputStream;
import com.volcengine.tos.model.object.TosObjectInputStream;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private static final Logger LOG = LoggerFactory.getLogger(RequestTransport.class);

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

        this.client = builder.dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .readTimeout(config.getReadTimeoutMills(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeoutMills(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectTimeoutMills(), TimeUnit.MILLISECONDS)
                // 默认关闭重定向
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }

    @Override
    public TosResponse roundTrip(TosRequest tosRequest) throws IOException {
        Request request = buildRequest(tosRequest);
        Response response = client.newCall(request).execute();
        InputStream inputStream = response.body() == null ? null : response.body().byteStream();
        return new TosResponse().setStatusCode(response.code())
                .setContentLength(getSize(response))
                .setHeaders(getHeaders(response))
                .setInputStream(inputStream);
    }

    private Request buildRequest(TosRequest request) throws IOException {
        HttpUrl url = request.toURL();
        Request.Builder builder = new Request.Builder().url(url);
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(builder::header);
        }

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
                throw new UnsupportedOperationException("Method is not supported: " + request.getMethod());
        }
        return builder.build();
    }


    private OkHttpClient.Builder ignoreCertificate(OkHttpClient.Builder builder) throws TosClientException {
        LOG.warn("ignore ssl certificate verification");
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
            LOG.warn("exception occurred while configuring ignoreSslCertificate");
            throw new TosClientException("set ignoreCertificate failed", e);
        }
        return builder;
    }

    private MediaType getMediaType(TosRequest request) {
        String type = StringUtils.EMPTY;
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
        Objects.requireNonNull(inputStream, "inputStream is null");
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