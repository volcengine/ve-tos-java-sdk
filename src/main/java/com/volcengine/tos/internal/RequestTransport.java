package com.volcengine.tos.internal;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CheckedInputStream;

import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.io.Retryable;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.model.RetryCountNotifier;
import com.volcengine.tos.internal.model.SimpleDataTransferListenInputStream;
import com.volcengine.tos.internal.model.TosCheckedInputStream;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.base64.Base64;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheServiceImpl;
import com.volcengine.tos.internal.util.ratelimit.RateLimitedInputStream;
import com.volcengine.tos.transport.TransportConfig;

/**
 * @author volcengine 1. Basic HTTP request and response handler 2. Retrier in
 * exception 3. Custom HTTP client config 4. DNS cache 5. Enable/Disable verify
 * SSL certification 6. HTTP proxy 7. Rate limiter 8. ...
 */
public class RequestTransport implements Transport, Closeable {

    private static final ContentType DEFAULT_MEDIA_TYPE = null;
    private final CloseableHttpClient client;
    private int maxRetries;

    private int except100ContinueThreshold;
    private boolean disableEncodingMeta;
    private DnsCacheService dnsCacheService;

    public RequestTransport(TransportConfig config) {
        ParamsChecker.ensureNotNull(config, "TransportConfig");
        int maxConnections = config.getMaxConnections() > 0 ? config.getMaxConnections() : Consts.DEFAULT_MAX_CONNECTIONS;
        int maxIdleConnectionTimeMills = config.getIdleConnectionTimeMills() > 0
                ? config.getIdleConnectionTimeMills() : Consts.DEFAULT_IDLE_CONNECTION_TIME_MILLS;
        int readTimeout = config.getReadTimeoutMills() >= 0 ? config.getReadTimeoutMills() : Consts.DEFAULT_READ_TIMEOUT_MILLS;
        int writeTimeout = config.getWriteTimeoutMills() >= 0 ? config.getWriteTimeoutMills() : Consts.DEFAULT_WRITE_TIMEOUT_MILLS;
        int connectTimeout = config.getConnectTimeoutMills() > 0 ? config.getConnectTimeoutMills() : Consts.DEFAULT_CONNECT_TIMEOUT_MILLS;

        this.maxRetries = config.getMaxRetryCount();
        if (maxRetries < 0) {
            maxRetries = 0;
        }
        this.except100ContinueThreshold = config.getExcept100ContinueThreshold();

        HttpClientBuilder builder = HttpClients.custom();

        RequestDnsResolver requestDnsResolver = new RequestDnsResolver(SystemDefaultDnsResolver.INSTANCE);

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
        if (!config.isHttp() && !config.isEnableVerifySSL()) {
            // the sdk verifies ssl cert while using https,
            // but if you disable ssl verification,
            // will ignore it by the following method.
            connectionManagerBuilder = TosUtils.ignoreCertificate(connectionManagerBuilder);

        }

        if (StringUtils.isNotEmpty(config.getProxyHost()) && config.getProxyPort() > 0) {
            addProxyConfig(config, builder);
        }

        if (config.getDnsCacheTimeMinutes() > 0) {
            dnsCacheService = new DnsCacheServiceImpl(config.getDnsCacheTimeMinutes(), 30);
            requestDnsResolver.setDnsCacheService(dnsCacheService);
        }

        connectionManagerBuilder.setDnsResolver(requestDnsResolver);
        connectionManagerBuilder.setMaxConnTotal(maxConnections);
        connectionManagerBuilder.setMaxConnPerRoute(maxConnections);

        ConnectionConfig.Builder conBuilder = ConnectionConfig.custom();
        conBuilder.setTimeToLive(maxIdleConnectionTimeMills, TimeUnit.MILLISECONDS);
        conBuilder.setSocketTimeout(Math.max(readTimeout, writeTimeout), TimeUnit.MILLISECONDS);
        conBuilder.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);

        connectionManagerBuilder.setDefaultConnectionConfig(conBuilder.build());

        RequestLatencyInterceptor requestLatencyInterceptor = new RequestLatencyInterceptor(TosUtils.getLogger())
                .setHighLatencyLogThreshold(config.getHighLatencyLogThreshold());
        RequestDnsInterceptor requestDnsInterceptor = new RequestDnsInterceptor(dnsCacheService);

        RequestConnectionManager requestConnectionManager = new RequestConnectionManager(connectionManagerBuilder.build());
        this.client = builder
                .setConnectionManager(requestConnectionManager)
                .setDefaultRequestConfig(requestConfigBuilder.build())
                .disableAutomaticRetries()
                .disableRedirectHandling()
                .addRequestInterceptorFirst(requestLatencyInterceptor)
                .addResponseInterceptorLast(requestLatencyInterceptor)
                .addExecInterceptorAfter("dnsRemover", "dnsRemover", requestDnsInterceptor)
                .disableContentCompression()
                .build();
    }

    private void addProxyConfig(TransportConfig config, HttpClientBuilder builder) {
        HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort());
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        builder.setRoutePlanner(routePlanner);
        if (StringUtils.isNotEmpty(config.getProxyUserName())) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(config.getProxyHost(), config.getProxyPort()),
                    new UsernamePasswordCredentials(config.getProxyUserName(), config.getProxyPassword().toCharArray()));
            builder.setDefaultCredentialsProvider(credentialsProvider);
        }
    }

    public RequestTransport setDisableEncodingMeta(boolean disableEncodingMeta) {
        this.disableEncodingMeta = disableEncodingMeta;
        return this;
    }

    @Override
    public void switchConfig(TransportConfig config) {
        // 先只支持重试次数修改
        maxRetries = config.getMaxRetryCount();
        if (maxRetries < 0) {
            maxRetries = 0;
        }
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    private class WrappedHttpResponse {

        private final TosResponse response;
        private final boolean retry;
        private final int code;
        private final String retryAfter;
        private final String reqID;
        private ClassicHttpResponse httpResponse;

        public WrappedHttpResponse(TosResponse response, boolean retry, int code, String retryAfter, String reqID) {
            this.response = response;
            this.retry = retry;
            this.code = code;
            this.retryAfter = retryAfter;
            this.reqID = reqID;
        }

        public TosResponse getResponse() {
            return this.response;
        }

        public boolean isRetry() {
            return this.retry;
        }

        public int getCode() {
            return this.code;
        }

        public String getRetryAfter() {
            return this.retryAfter;
        }

        public String getReqID() {
            return this.reqID;
        }

        public void setHttpResponse(ClassicHttpResponse httpResponse) {
            this.httpResponse = httpResponse;
        }

        public ClassicHttpResponse getHttpResponse() {
            return this.httpResponse;
        }
    }

    @Override
    public TosResponse roundTrip(TosRequest tosRequest) throws IOException {
        WrappedHttpResponse wrappedHttpResponse = null;
        long start = System.currentTimeMillis();
        int reqTimes = 1;
        wrapTosRequestContent(tosRequest);
        ClassicHttpRequest lastRequest = null;
        for (int i = 0; i < maxRetries + 1; i++, reqTimes++) {
            try {
                if (tosRequest.getContent() != null && (tosRequest.getContent() instanceof RetryCountNotifier)) {
                    ((RetryCountNotifier) tosRequest.getContent()).setRetryCount(i);
                }

                if (lastRequest != null) {
                    HttpEntity entity = lastRequest.getEntity();
                    if (entity != null && entity instanceof WrappedApacheTransportRequestBody) {
                        ((WrappedApacheTransportRequestBody) entity).reset();
                    }
                }

                ClassicHttpRequest builder = buildRequest(tosRequest);
                lastRequest = builder;
                if (i != 0) {
                    builder.addHeader(TosHeader.HEADER_SDK_RETRY_COUNT, "attempt=" + i + "; max=" + maxRetries);
                }

                ClassicHttpResponse response = this.client.executeOpen(null, builder, null);
                wrappedHttpResponse = this.handleResponse(response, tosRequest);
                wrappedHttpResponse.setHttpResponse(response);
                if (wrappedHttpResponse.isRetry()) {
                    if (tosRequest.isRetryableOnServerException()) {
                        // the request can be retried.
                        if (i != maxRetries) {
                            long sleepMs = TosUtils.backoff(i);
                            if (wrappedHttpResponse.getCode() == HttpStatus.SERVICE_UNAVAILABLE || wrappedHttpResponse.getCode() == HttpStatus.TOO_MANY_REQUESTS) {
                                if (StringUtils.isNotEmpty(wrappedHttpResponse.getRetryAfter())) {
                                    try {
                                        sleepMs = Math.max(Integer.parseInt(wrappedHttpResponse.getRetryAfter()) * 1000, sleepMs);
                                    } catch (NumberFormatException ex) {
                                    }
                                }
                            }
                            // last time does not need to sleep
                            Thread.sleep(sleepMs);
                            continue;
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                TosUtils.getLogger().debug("tos: request interrupted while sleeping in retry");
                printAccessLogFailed(e);
                throw new TosClientException("tos: request interrupted", e);
            } catch (IOException e) {
                if (tosRequest.isRetryableOnClientException() && !"mark/reset not supported".equals(e.toString())) {
                    try {
                        if (i == maxRetries) {
                            // last time does not need to sleep
                            printAccessLogFailed(e);
                            throw e;
                        }
                        Thread.sleep(TosUtils.backoff(i));
                        continue;
                    } catch (InterruptedException ie) {
                        TosUtils.getLogger().debug("tos: request interrupted while sleeping in retry");
                        printAccessLogFailed(e);
                        throw new TosClientException("tos: request interrupted", e);
                    }
                }
                printAccessLogFailed(e);
                throw e;
            } catch (URISyntaxException e) {
                TosUtils.getLogger().debug("tos: request interrupted while sleeping in retry");
                printAccessLogFailed(e);
                throw new TosClientException("tos: request interrupted", e);
            }
        }
        long end = System.currentTimeMillis();
        if (wrappedHttpResponse == null) {
            throw new TosClientException("empty wrappedHttpResponse", null);
        }
        ParamsChecker.ensureNotNull(wrappedHttpResponse.getHttpResponse(), "okhttp response");
        printAccessLogSucceed(wrappedHttpResponse.getCode(), wrappedHttpResponse.getReqID(), end - start, reqTimes);
        if (wrappedHttpResponse.getResponse() == null) {
            // means retry also error, return code and header out
            return new TosResponse().setStatusCode(wrappedHttpResponse.getCode())
                    .setContentLength(getSize(wrappedHttpResponse.getHttpResponse()))
                    .setHeaders(getHeaders(wrappedHttpResponse.getHttpResponse()))
                    .setInputStream(null);
        }
        return wrappedHttpResponse.getResponse();
    }

    private void printAccessLogSucceed(int code, String reqId, long cost, int reqTimes) {
        TosUtils.getLogger().info("tos: status code:{}, request id:{}, request cost {} ms, request {} times\n",
                code, reqId, cost, reqTimes);
    }

    private void printAccessLogFailed(Exception e) {
        TosUtils.getLogger().info("tos: request exception: {}\n", e.toString());
    }

    private void checkCrc(TosRequest tosRequest, ClassicHttpResponse response) {
        boolean needCheckCrc = tosRequest.isEnableCrcCheck()
                && response.getCode() < HttpStatus.MULTIPLE_CHOICE
                && tosRequest.getContent() != null
                && tosRequest.getContent() instanceof CheckedInputStream;
        if (!needCheckCrc) {
            return;
        }
        // request successfully, check crc64
        long clientCrcLong = ((CheckedInputStream) tosRequest.getContent()).getChecksum().getValue();
        String clientHashCrc64Ecma = CRC64Utils.longToUnsignedLongString(clientCrcLong);
        String serverHashCrc64Ecma = response.getFirstHeader(TosHeader.HEADER_CRC64).getValue();
        if (StringUtils.isNotEmpty(serverHashCrc64Ecma) && !StringUtils.equals(clientHashCrc64Ecma, serverHashCrc64Ecma)) {
            throw new TosClientException("tos: crc64 check failed, "
                    + "expected:" + serverHashCrc64Ecma
                    + ", in fact:" + clientHashCrc64Ecma,
                    null);
        }
    }

    private WrappedHttpResponse handleResponse(ClassicHttpResponse response, TosRequest tosRequest) throws IOException {
        ParamsChecker.ensureNotNull(response, "http response");
        int code = response.getCode();
        String reqID = null;
        String headEC = null;
        if (response.getFirstHeader(TosHeader.HEADER_REQUEST_ID) != null) {
            reqID = response.getFirstHeader(TosHeader.HEADER_REQUEST_ID).getValue();
        }

        if (response.getFirstHeader(TosHeader.HEADER_EC) != null) {
            headEC = response.getFirstHeader(TosHeader.HEADER_EC).getValue();
        }

        if (code >= HttpStatus.INTERNAL_SERVER_ERROR
                || code == HttpStatus.TOO_MANY_REQUESTS
                || code == HttpStatus.REQUEST_TIMEOUT
                || (code == HttpStatus.BAD_REQUEST && "0005-00000044".equals(headEC))) {
            if (response.getFirstHeader(TosHeader.HEADER_RETRY_AFTER) == null) {
                // retry on 5xx, 429, 400+0005-00000044
                return new WrappedHttpResponse(null, true, code, null, reqID);
            }
            String retryAfter = response.getFirstHeader(TosHeader.HEADER_RETRY_AFTER).getValue();
            // retry on 5xx, 429, 400+0005-00000044
            return new WrappedHttpResponse(null, true, code, retryAfter, reqID);
        }
        ParamsChecker.ensureNotNull(response, "http response");
        checkCrc(tosRequest, response);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity == null ? null : entity.getContent();
        return new WrappedHttpResponse(new TosResponse().setStatusCode(code)
                .setContentLength(getSize(response))
                .setHeaders(getHeaders(response))
                .setInputStream(inputStream), false, code, null, reqID);
    }

    private ClassicHttpRequest buildRequest(TosRequest request) throws IOException, URISyntaxException {
        URI uri = request.toURL();
        ClassicHttpRequest builder;
        switch (request.getMethod() == null ? "" : request.getMethod().toUpperCase()) {
            case HttpMethod.GET:
                builder = new HttpGet(uri);
                break;
            case HttpMethod.POST:
                builder = new HttpPost(uri);
                if (request.getContent() != null && request.getContentLength() <= 0) {
                    // 兼容 ClientV1 旧接口，有bug，ClientV2 新接口不会走到这里
                    byte[] data = new byte[request.getContent().available()];
                    int exact = request.getContent().read(data);
                    if (exact != -1 && exact != data.length) {
                        throw new IOException("expected " + data.length + " bytes, but got " + exact + " bytes.");
                    }
                    builder.setEntity(new ByteArrayEntity(data, getContentType(request)));
                } else if (request.getContent() != null) {
                    if (this.except100ContinueThreshold > 0 && (request.getContentLength() < 0
                            || request.getContentLength() > this.except100ContinueThreshold)) {
                        builder.addHeader(TosHeader.HEADER_EXPECT, "100-continue");
                    }
                    // only appendObject use, not support chunk
                    // make sure the content length is set
                    builder.setEntity(new WrappedApacheTransportRequestBody(getContentType(request), request));
                } else if (request.getData() != null) {
                    if (this.except100ContinueThreshold > 0 && request.getData().length > this.except100ContinueThreshold) {
                        builder.addHeader(TosHeader.HEADER_EXPECT, "100-continue");
                    }
                    builder.setEntity(new ByteArrayEntity(request.getData(), getContentType(request)));
                } else {
                    builder.setEntity(new ByteArrayEntity(new byte[0], getContentType(request)));
                }
                break;
            case HttpMethod.PUT: {
                builder = new HttpPut(uri);
                if (request.getContent() != null) {
                    if (this.except100ContinueThreshold > 0 && (request.getContentLength() < 0
                            || request.getContentLength() > this.except100ContinueThreshold)) {
                        builder.addHeader(TosHeader.HEADER_EXPECT, "100-continue");
                    }
                    builder.setEntity(new WrappedApacheTransportRequestBody(getContentType(request), request));
                } else if (request.getData() != null) {
                    if (this.except100ContinueThreshold > 0 && request.getData().length > this.except100ContinueThreshold) {
                        builder.addHeader(TosHeader.HEADER_EXPECT, "100-continue");
                    }
                    builder.setEntity(new ByteArrayEntity(request.getData(), getContentType(request)));
                } else {
                    builder.setEntity(new ByteArrayEntity(new byte[0], getContentType(request)));
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
                throw new TosClientException("Method is not supported: " + request.getMethod(), null);
        }
        addHeader(request, builder);
        return builder;
    }

    private void addHeader(TosRequest request, ClassicHttpRequest builder) {
        if (request == null || builder == null || request.getHeaders() == null) {
            return;
        }
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            String key = entry.getKey();
            if (key == TosHeader.HEADER_CONTENT_LENGTH) {
                // content-length will be calculated by set entity
                continue;
            }
            String value = entry.getValue();
            builder.addHeader(key, value);
        }
    }

    private void wrapTosRequestContent(TosRequest request) {
        if (request == null || request.getContent() == null) {
            return;
        }
        // 确保 TosRequest 拿到的 InputStream 为外部传入，没有封装过，统一在此方法中进行封装
        InputStream originalInputStream = request.getContent();
        InputStream wrappedInputStream = null;
        int readLimit = Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE;
        if (request.getReadLimit() > 0) {
            readLimit = request.getReadLimit();
        }
        if (originalInputStream.markSupported()) {
            // 流本身支持 mark&reset，不做封装
            wrappedInputStream = originalInputStream;
        } else {
            if (originalInputStream instanceof FileInputStream) {
                // 文件流封装成可重试的流
                wrappedInputStream = new TosRepeatableFileInputStream((FileInputStream) originalInputStream);
            } else {
                wrappedInputStream = new BufferedInputStream(originalInputStream, readLimit);
            }
        }

        wrappedInputStream.mark(readLimit);
        if (request.getRateLimiter() != null) {
            wrappedInputStream = new RateLimitedInputStream(wrappedInputStream, request.getRateLimiter());
        }
        if (request.getDataTransferListener() != null) {
            wrappedInputStream = new SimpleDataTransferListenInputStream(wrappedInputStream,
                    request.getDataTransferListener(), request.getContentLength());
        }

        if (request.isUseTrailerHeader() || request.isEnableCrcCheck()) {
            // 此封装需保证放最外层，因为上传后需要对上传结果的 crc 进行校验。
            CRC64Checksum checksum = new CRC64Checksum(request.getCrc64InitValue());
            wrappedInputStream = new TosCheckedInputStream(wrappedInputStream, checksum);
        }
        request.setContent(wrappedInputStream);
    }

    private ContentType getContentType(TosRequest request) {
        String type = "";
        if (request.getHeaders() != null && request.getHeaders().containsKey(TosHeader.HEADER_CONTENT_TYPE)) {
            type = request.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        }
        return StringUtils.isEmpty(type) ? DEFAULT_MEDIA_TYPE : ContentType.parse(type);
    }

    private long getSize(ClassicHttpResponse response) {
        Header header = response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH);
        if (header == null) {
            return 0;
        }
        String size = header.getValue();
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
        if (!this.disableEncodingMeta) {
            // 在此统一处理 header 的解码
            if (StringUtils.startWithIgnoreCase(key, TosHeader.HEADER_META_PREFIX)) {
                // 对于自定义元数据，对 key/value 包含的中文汉字进行 URL 解码
                key = TosUtils.decodeHeader(key);
                value = TosUtils.decodeHeader(value);
            } else if (StringUtils.equalsIgnoreCase(key, TosHeader.HEADER_CONTENT_DISPOSITION)) {
                // 对于 Content-Disposition 头，对 value 包含的中文汉字进行 URL 解码
                value = TosUtils.decodeHeader(value);
            }
        }
        headers.put(key.toLowerCase(), value);
    }

    @Override
    public void close() throws IOException {
        if (this.dnsCacheService != null && this.dnsCacheService instanceof Closeable) {
            ((Closeable) this.dnsCacheService).close();
        }
        if (this.client != null) {
            this.client.close();
        }
    }
}

class WrappedApacheTransportRequestBody extends AbstractHttpEntity implements Closeable {

    private final InputStream content;
    private final ContentType contentType;
    private final boolean useTrailerHeader;
    private long contentLength;
    private long decodedContentLength;
    private volatile long totalBytesRead = 0;

    WrappedApacheTransportRequestBody(final ContentType contentType, final InputStream content, final long contentLength) {
        super(contentType, null);
        ParamsChecker.ensureNotNull(content, "Content");
        this.content = content;
        this.contentType = contentType;
        this.contentLength = contentLength;
        if (this.contentLength < 0) {
            // chunked
            this.contentLength = -1L;
        }
        this.decodedContentLength = this.contentLength;
        this.useTrailerHeader = false;
    }

    WrappedApacheTransportRequestBody(ContentType contentType, TosRequest request) {
        super(contentType, null);
        ParamsChecker.ensureNotNull(request.getContent(), "Content");
        this.content = request.getContent();
        this.contentType = contentType;
        this.contentLength = request.getContentLength();
        if (this.contentLength < 0) {
            // chunked
            this.contentLength = -1L;
        }
        this.useTrailerHeader = request.isUseTrailerHeader();
        this.decodedContentLength = this.contentLength;
        if (this.useTrailerHeader && request.getHeaders() != null && request.getHeaders().containsKey(TosHeader.HEADER_DECODED_CONTENT_LENGTH)) {
            this.decodedContentLength = Long.parseLong(request.getHeaders().get(TosHeader.HEADER_DECODED_CONTENT_LENGTH));
        }
    }

    @Override
    public boolean isRepeatable() {
        return this.content.markSupported() || this.content instanceof Retryable;
    }

    @Override
    public long getContentLength() {
        return this.contentLength;
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return this.content;
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        this.reset();
        if (this.contentLength < 0) {
            if (this.useTrailerHeader) {
                this.writeAllWithChunkedWithTrailerHeader(outStream);
                this.writeTosChunkedTrailer(outStream);
                this.writeTrailerHeader(outStream);
            } else {
                writeAllWithChunked(outStream);
            }
        } else {
            long remaining = this.decodedContentLength;
            if (this.useTrailerHeader) {
                this.writeTosChunkedHeader(outStream, remaining);
                this.writeAll(outStream, remaining);
                this.writeTosChunkedTrailer(outStream);
                this.writeTrailerHeader(outStream);
            } else {
                this.writeAll(outStream, remaining);
            }
        }
        if (this.decodedContentLength >= 0 && this.decodedContentLength > totalBytesRead) {
            throw new IOException("tos: content length inconsistent, totalBytesRead: " + totalBytesRead + ", contentLength: " + this.decodedContentLength);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
        }
    }

    void writeTosChunkedHeader(OutputStream outStream, long chunkSize) throws IOException {
        outStream.write(this.toUft8(Long.toHexString(chunkSize)));
        outStream.write('\r');
        outStream.write('\n');
    }

    void writeTosChunkedTrailer(OutputStream outStream) throws IOException {
        if (this.totalBytesRead > 0) {
            outStream.write('\r');
            outStream.write('\n');
        }
        this.writeTosChunkedHeader(outStream, 0);
    }

    void writeTrailerHeader(OutputStream outStream) throws IOException {
        outStream.write(toUft8(TosHeader.HEADER_CRC64));
        outStream.write(toUft8(":"));
        long crc64 = ((CheckedInputStream) this.content).getChecksum().getValue();
        outStream.write(Base64.encodeBase64(TosUtils.longToByteArray(crc64)));
        outStream.write('\r');
        outStream.write('\n');
        outStream.write('\r');
        outStream.write('\n');
    }

    void reset() throws IOException {
        if (totalBytesRead > 0 && this.content != null) {
            if (this.content.markSupported()) {
                TosUtils.getLogger().debug("tos: apache http writeTo call reset");
                this.content.reset();
                totalBytesRead = 0;
            } else if (this.content instanceof Retryable) {
                TosUtils.getLogger().debug("tos: apache http writeTo call reset");
                ((Retryable) this.content).reset();
                totalBytesRead = 0;
            }
        }
    }

    byte[] toUft8(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    private void writeAll(OutputStream outStream, long remaining) throws IOException {
        int bytesRead;
        byte[] tmp = new byte[Consts.DEFAULT_READ_BUFFER_SIZE];
        while (remaining > 0) {
            int maxToRead = tmp.length < remaining ? tmp.length : (int) remaining;
            bytesRead = this.content.read(tmp, 0, maxToRead);
            if (bytesRead == -1) {
                // eof
                break;
            }
            outStream.write(tmp, 0, bytesRead);
            totalBytesRead += bytesRead;
            remaining -= bytesRead;
        }
    }

    private void writeAllWithChunked(OutputStream outStream) throws IOException {
        int bytesRead;
        byte[] tmp = new byte[Consts.DEFAULT_READ_BUFFER_SIZE];
        // chunked
        bytesRead = this.content.read(tmp);
        while (bytesRead != -1) {
            outStream.write(tmp, 0, bytesRead);
            totalBytesRead += bytesRead;
            bytesRead = this.content.read(tmp);
        }
    }

    private void writeAllWithChunkedWithTrailerHeader(OutputStream outStream) throws IOException {
        int bytesRead;
        byte[] tmp = new byte[Consts.DEFAULT_TOS_CHUNK_SIZE];
        bytesRead = this.content.read(tmp);
        while (bytesRead != -1) {
            this.writeTosChunkedHeader(outStream, bytesRead);
            outStream.write(tmp, 0, bytesRead);
            totalBytesRead += bytesRead;
            bytesRead = this.content.read(tmp);
        }
    }

    public ContentType contentType() {
        return this.contentType;
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
}
