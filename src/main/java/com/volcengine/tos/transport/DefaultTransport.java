package com.volcengine.tos.transport;

import com.volcengine.tos.TosRequest;
import com.volcengine.tos.TosResponse;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.TosHeader;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DefaultTransport implements Transport{
    private static final MediaType DEFAULT_MEDIA_TYPE = null;
    private final OkHttpClient client;

    public DefaultTransport(TransportConfig config){
        ConnectionPool connectionPool = new ConnectionPool(config.getMaxIdleCount(),
                config.getKeepAlive(), TimeUnit.SECONDS);

        Dispatcher dispatcher = new Dispatcher();
        this.client = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(false)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .build();
    }


    private MediaType getMediaType(TosRequest request) {
        String type = StringUtils.EMPTY;
        if (request.getHeaders() != null) {
            type = request.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        }
        return StringUtils.isEmpty(type) ? DEFAULT_MEDIA_TYPE : MediaType.parse(type);
    }

    @Override
    public TosResponse roundTrip(TosRequest request) throws IOException {
        Objects.requireNonNull(request.getScheme(), "scheme is null");
        Objects.requireNonNull(request.getHost(), "host is null");
        HttpUrl url = request.toURL();
        Objects.requireNonNull(url, "url is null");
        Request.Builder builder = new Request.Builder().url(url);
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(builder::header);
        }

        switch (request.getMethod() == null ? "" : request.getMethod().toUpperCase()) {
            case HttpMethod.GET:
                builder.get();
                break;
            case HttpMethod.POST:
                if (request.getContent() != null){
                    byte[] data = new byte[request.getContent().available()];
                    // Warning 当输入流是网络IO时，这里可能会出错
                    int exact = request.getContent().read(data);
                    if (exact != data.length) {
                        throw new IOException("expected "+data.length+" bytes, but got "+exact+" bytes.");
                    }
                    builder.post(RequestBody.create(getMediaType(request), data));
                } else{
                    builder.post(RequestBody.create(getMediaType(request), request.getData()));
                }
                break;
            case HttpMethod.PUT: {
                if (request.getContent() != null) {
                    builder.put(new InputStreamRequestBody(getMediaType(request), request.getContent()));
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
        Response response = client.newCall(builder.build()).execute();
        InputStream inputStream = response.body() == null ? null : response.body().byteStream();
        return new TosResponse().setStatusCode(response.code())
                .setContentLength(getSize(response))
                .setHeaders(getHeaders(response))
                .setInputStream(inputStream);
    }

    private long getSize(Response response) {
        String size = response.header(TosHeader.HEADER_CONTENT_LENGTH);
        if (StringUtils.isEmpty(size)) {
            return 0;
        }
        return Long.parseLong(size);
    }

    private Map<String, String> getHeaders(Response response) {
        Map<String, String> headers = new HashMap<>();
        response.headers().names().forEach(
                (name) -> headers.put(formatHeadersName(name), response.header(name))
        );
        return headers;
    }

    String formatHeadersName(String name){
        if (name == null || name.length() == 0) {
            return "";
        }
        name = name.toLowerCase();
        if (name.startsWith(TosHeader.HEADER_META_PREFIX.toLowerCase())) {
            return TosHeader.HEADER_META_PREFIX;
        }
        if (Objects.equals(name, TosHeader.HEADER_ETAG.toLowerCase())) {
            return TosHeader.HEADER_ETAG;
        }
        String[] subs = name.split("-");
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < subs.length; i++) {
            char[] cs = subs[i].toCharArray();
            if (cs[0] >= 'a' && cs[0] <= 'z'){
                cs[0] = (char)(cs[0]-32);
            }
            res.append(cs);
            if (i != subs.length-1){
                res.append('-');
            }
        }
        return res.toString();
    }
}
