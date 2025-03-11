package com.volcengine.tos.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;

import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.TosRequest;
import com.volcengine.tos.internal.TosResponse;
import com.volcengine.tos.internal.Transport;
import com.volcengine.tos.internal.util.StringUtils;

@Deprecated
public class DefaultTransport implements Transport {

    private static final ContentType DEFAULT_MEDIA_TYPE = null;
    private final CloseableHttpClient client;

    public DefaultTransport(TransportConfig config) {
        HttpClientBuilder builder = HttpClients.custom();
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setTimeToLive(config.getKeepAlive(), TimeUnit.SECONDS)
                .setConnectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .setSocketTimeout(Math.max(config.getReadTimeout(), config.getWriteTimeout()), TimeUnit.SECONDS)
                .build();

        PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
        PoolingHttpClientConnectionManager connectionManager = connectionManagerBuilder
                .build();
        connectionManager.setMaxTotal(config.getMaxIdleCount());
        connectionManager.setDefaultMaxPerRoute(config.getMaxIdleCount());
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        RequestConfig requestConfig = RequestConfig.custom()
                .build();

        this.client = builder
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableAutomaticRetries().disableContentCompression()
                .build();
    }

    @Override
    public void switchConfig(TransportConfig config) {
    }

    private ContentType getMediaType(TosRequest request) {
        String type = "";
        if (request.getHeaders() != null) {
            type = request.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        }
        return StringUtils.isEmpty(type) ? DEFAULT_MEDIA_TYPE : ContentType.parse(type);
    }

    @Override
    public TosResponse roundTrip(TosRequest request) throws IOException {
        Objects.requireNonNull(request.getScheme(), "scheme is null");
        Objects.requireNonNull(request.getHost(), "host is null");
        URI uri;
        try {
            uri = request.toURL();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        Objects.requireNonNull(uri, "url is null");
        ClassicHttpRequest builder;
        switch (request.getMethod() == null ? "" : request.getMethod().toUpperCase()) {
            case HttpMethod.GET:
                builder = new HttpGet(uri);
                break;
            case HttpMethod.POST:
                builder = new HttpPost(uri);
                if (request.getContent() != null) {
                    byte[] data = new byte[request.getContent().available()];
                    // Warning 当输入流是网络IO时，这里可能会出错
                    int exact = request.getContent().read(data);
                    if (exact != data.length) {
                        throw new IOException("expected " + data.length + " bytes, but got " + exact + " bytes.");
                    }
                    builder.setEntity(new ByteArrayEntity(data, getMediaType(request)));
                } else {
                    builder.setEntity(new ByteArrayEntity(request.getData(), getMediaType(request)));
                }
                break;
            case HttpMethod.PUT: {
                builder = new HttpPut(uri);
                if (request.getContent() != null) {
                    builder.setEntity(new InputStreamEntity(request.getContent(), request.getContentLength(), getMediaType(request)));
                } else {
                    builder.setEntity(new ByteArrayEntity(request.getData(), getMediaType(request)));
                }
                break;
            }
            case HttpMethod.HEAD:
                builder = new HttpHead(uri);
                break;
            case HttpMethod.DELETE:
                builder = new HttpDelete(uri);
                break;
            default:
                throw new UnsupportedOperationException("Method is not supported: " + request.getMethod());
        }

        addHeader(request, builder);
        ClassicHttpResponse response = client.execute(builder);
        InputStream inputStream = response.getEntity() == null ? null : response.getEntity().getContent();
        return new TosResponse().setStatusCode(response.getCode())
                .setContentLength(getSize(response))
                .setHeaders(getHeaders(response))
                .setInputStream(inputStream);
    }

    private long getSize(ClassicHttpResponse response) {
        String size = null;
        if (response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH) != null) {
            size = size = response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue();
        }
        if (StringUtils.isEmpty(size)) {
            return 0;
        }
        return Long.parseLong(size);
    }

    private Map<String, String> getHeaders(ClassicHttpResponse response) {
        Header[] headers = response.getHeaders();
        Map<String, String> headersMap = new HashMap<>(headers.length);
        for (Header head : response.getHeaders()) {
            parseHeader(response, headersMap, head.getName());
        }
        return headersMap;
    }

    private void parseHeader(ClassicHttpResponse response, Map<String, String> headers, String name) {
        // 原始的 key/value 值
        String key = name;
        String value = response.getFirstHeader(name).getValue();
        headers.put(key.toLowerCase(), value);
    }

    private void addHeader(TosRequest request, ClassicHttpRequest builder) {
        if (request == null || builder == null || request.getHeaders() == null) {
            return;
        }
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addHeader(key, value);
        }
    }
}
