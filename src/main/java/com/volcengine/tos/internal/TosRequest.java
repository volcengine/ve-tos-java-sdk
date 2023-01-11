package com.volcengine.tos.internal;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableInputStream;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import okhttp3.HttpUrl;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class TosRequest {
    private String scheme;
    private String method;
    private String host;
    private String path;
    private int port;
    private long contentLength;
    private boolean retryableOnClientException;
    private boolean retryableOnServerException;
    /**
     * only used for putObject/uploadPart/appendObject
     */
    private boolean enableCrcCheck;
    /**
     * only used for appendObject
     */
    private long crc64InitValue;
    private transient InputStream content;
    private RateLimiter rateLimiter;
    private DataTransferListener dataTransferListener;
    private Map<String, String> headers = Collections.emptyMap();
    private Map<String, String> query = Collections.emptyMap();

    /**
     *  only for POST data
     *  only used in ClientV1, deprecated in ClientV2
      */
    private byte[] data = new byte[0];

    public TosRequest(){

    }
    public TosRequest(String scheme, String method, String host, String path){
        this.scheme = scheme;
        this.method = method;
        this.host = host;
        this.path = path;
    }
    public TosRequest(String scheme, String method, String host, String path, InputStream inputStream,
                      Map<String, String> query, Map<String, String> headers){
        this.scheme = scheme;
        this.method = method;
        this.host = host;
        this.path = path;
        if (inputStream != null) {
            this.content = new TosRepeatableInputStream(inputStream, Consts.DEFAULT_READ_BUFFER_SIZE);
        } else {
            this.content = null;
        }
        if (inputStream instanceof TosRepeatableInputStream || inputStream instanceof TosRepeatableFileInputStream
                || inputStream instanceof TosRepeatableBoundedFileInputStream) {
            // 这几种不用转换
            this.content = inputStream;
        }
        this.query = query;
        this.headers = headers;
        // 默认可重试
        this.retryableOnClientException = true;
        this.retryableOnServerException = true;
    }

    public HttpUrl toURL() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        if (query != null) {
            for (Map.Entry<String, String> entry : query.entrySet()) {
                builder.addEncodedQueryParameter(entry.getKey(), TosUtils.uriEncode(entry.getValue(), true));
            }
        }
        // path 带了'/'，addPathSegment 会自动添加'/'，因此这里移除之
        String escapePath = StringUtils.removeStart(path, "/");
        builder = builder.scheme(scheme).host(host).addPathSegment(escapePath);
        if (port != 0) {
            builder.port(port);
        }
        return builder.build();
    }

    public String getScheme() {
        return scheme;
    }

    public TosRequest setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public TosRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TosRequest setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPath() {
        return path;
    }

    public TosRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public TosRequest setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public TosRequest setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public TosRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public TosRequest setQuery(Map<String, String> query) {
        this.query = query;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    @Deprecated
    public TosRequest setData(byte[] data) {
        this.data = data;
        return this;
    }

    public int getPort() {
        return port;
    }

    public TosRequest setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean isRetryableOnClientException() {
        return retryableOnClientException;
    }

    public TosRequest setRetryableOnClientException(boolean retryableOnClientException) {
        this.retryableOnClientException = retryableOnClientException;
        return this;
    }

    public boolean isRetryableOnServerException() {
        return retryableOnServerException;
    }

    public TosRequest setRetryableOnServerException(boolean retryableOnServerException) {
        this.retryableOnServerException = retryableOnServerException;
        return this;
    }

    public boolean isEnableCrcCheck() {
        return enableCrcCheck;
    }

    public TosRequest setEnableCrcCheck(boolean enableCrcCheck) {
        this.enableCrcCheck = enableCrcCheck;
        return this;
    }

    public long getCrc64InitValue() {
        return crc64InitValue;
    }

    public TosRequest setCrc64InitValue(long crc64InitValue) {
        this.crc64InitValue = crc64InitValue;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public TosRequest setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public TosRequest setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }
}
