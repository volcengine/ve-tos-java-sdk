package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.model.HttpRange;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.volcengine.tos.internal.Consts.*;

public class RequestBuilder {
    private Signer signer;
    private String scheme;
    private String host;
    private int port;
    private String bucket;
    private String object;
    private int urlMode = URL_MODE_DEFAULT;
    private long contentLength = -1;
    private HttpRange range;
    private Map<String, String> headers = new HashMap<>(1);
    private Map<String, String> query;
    private boolean autoRecognizeContentType = true;
    private String preHashCrc64ecma;
    private boolean disableEncodingMeta;
    private boolean skipTryResolveContentLength;

    public RequestBuilder(){}

    @Deprecated
    public RequestBuilder(Signer signer, String scheme, String host, String bucket, String object,
                   int urlMode, Map<String, String> headers, Map<String, String> query){
        Objects.requireNonNull(bucket, "bucket is null");
        Objects.requireNonNull(object, "object is null");
        this.signer = signer;
        this.scheme = scheme;
        this.host = host;
        this.bucket = bucket;
        this.object = object;
        this.urlMode = urlMode;
        this.headers = headers;
        this.query = query;
    }

    public RequestBuilder(String bucket, String object, String scheme, String host, Signer signer){
        Objects.requireNonNull(scheme, "scheme is null");
        Objects.requireNonNull(host, "host is null");
        // bucket can be null
//        Objects.requireNonNull(bucket, "bucket is null");
        this.bucket = bucket;
        this.object = object;
        this.scheme = scheme;
        this.host = host;
        this.signer = signer;
    }

    public RequestBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public RequestBuilder setQuery(Map<String, String> query) {
        this.query = query;
        return this;
    }

    public HttpRange getRange() {
        return range;
    }

    void setRange(HttpRange range) {
        this.range = range;
    }

    public RequestBuilder withQuery(String key, String value){
        if (StringUtils.isEmpty(key) || value == null) {
            return this;
        }
        if (this.query == null) {
            this.query = new HashMap<>(1);
        }
        this.query.put(key, value);
        return this;
    }

    public RequestBuilder withHeader(String key, String value){
        if(value != null && value.length() != 0){
            this.headers.put(key, value);
        }
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean isAutoRecognizeContentType() {
        return autoRecognizeContentType;
    }

    public void setAutoRecognizeContentType(boolean autoRecognizeContentType) {
        this.autoRecognizeContentType = autoRecognizeContentType;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public RequestBuilder withContentLength(long length){
        this.contentLength = length;
        return this;
    }

    public String getHost() {
        return host;
    }

    public RequestBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public int getUrlMode() {
        return urlMode;
    }

    public RequestBuilder setUrlMode(int urlMode) {
        this.urlMode = urlMode;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RequestBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public String getPreHashCrc64ecma() {
        return preHashCrc64ecma;
    }

    public RequestBuilder setPreHashCrc64ecma(String preHashCrc64ecma) {
        this.preHashCrc64ecma = preHashCrc64ecma;
        return this;
    }

    public RequestBuilder setDisableEncodingMeta(boolean disableEncodingMeta) {
        this.disableEncodingMeta = disableEncodingMeta;
        return this;
    }

    public RequestBuilder setSkipTryResolveContentLength(boolean skipTryResolveContentLength) {
        this.skipTryResolveContentLength = skipTryResolveContentLength;
        return this;
    }

    private TosRequest build(String method, InputStream stream) throws IOException {
        TosRequest request = genTosRequest(method, stream);
        if (this.skipTryResolveContentLength) {
            request.setContentLength(this.contentLength);
        } else if (request.getContent() != null) {
            tryResolveContentLength(request);
        }
        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() > 0) {
            tryEncodeHeaders(headers);
        }
        return request;
    }

    private void tryResolveContentLength(TosRequest request) {
        if (this.contentLength >= 0) {
            request.setContentLength(contentLength);
        } else if (StringUtils.isNotEmpty(headers.get(TosHeader.HEADER_CONTENT_LENGTH))) {
            try{
                long cl = Long.parseLong(headers.get(TosHeader.HEADER_CONTENT_LENGTH));
                request.setContentLength(cl >= 0 ? cl : -1L);
            } catch (NumberFormatException e) {
                TosUtils.getLogger().debug("tos: try to get content length from header failed, ", e);
                request.setContentLength(-1L);
            }
        } else {
            request.setContentLength(-1L);
        }
        if (request.getContent() instanceof FileInputStream && request.getContentLength() <= 0) {
            // 文件流，尝试获取文件长度
            try {
                FileChannel channel = ((FileInputStream) request.getContent()).getChannel();
                request.setContentLength(channel.size());
            } catch (IOException e) {
                TosUtils.getLogger().debug("tos: try to get content length from file failed, ", e);
                request.setContentLength(-1L);
            }
        }
    }

    private void tryEncodeHeaders(Map<String, String> headers) {
        Map<String, String> encodedHeaders = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            // 在此统一处理 header 的编码
            if (StringUtils.isNotEmpty(key) && key.startsWith(TosHeader.HEADER_META_PREFIX)) {
                if (this.disableEncodingMeta) {
                    encodedHeaders.put(key, value);
                } else {
                    // 对于自定义元数据，对 key/value 包含的中文汉字进行 URL 编码
                    encodedHeaders.put(TosUtils.encodeHeader(key), TosUtils.encodeHeader(value));
                }
                iterator.remove();
            } else if (StringUtils.equals(key, TosHeader.HEADER_CONTENT_DISPOSITION)) {
                if (this.disableEncodingMeta) {
                    encodedHeaders.put(key, value);
                } else {
                    // 对于 Content-Disposition 头，对 value 包含的中文汉字进行 URL 编码
                    encodedHeaders.put(key, TosUtils.encodeChinese(value));
                }
                iterator.remove();
            }
        }
        headers.putAll(encodedHeaders);
    }

    private TosRequest genTosRequest(String method, InputStream stream) {
        String[] hostAndPath = hostPath();
        TosRequest request = new TosRequest(scheme, method, hostAndPath[0], hostAndPath[1], stream, query, headers);
        if (this.port != 0) {
            request.setPort(this.port);
        }
        return request;
    }

    private String[] hostPath(){
        String[] hostAndPath = new String[]{"", ""};
        if (urlMode == URL_MODE_CUSTOM_DOMAIN) {
            hostAndPath[0] = this.host;
            hostAndPath[1] = "/" + this.object;
        } else if (urlMode == URL_MODE_PATH) {
            hostAndPath[0] = this.host;
            hostAndPath[1] = "/" + this.bucket + "/" + this.object;
        } else if (StringUtils.isEmpty(this.bucket)){
            hostAndPath[0] = this.host;
            hostAndPath[1] = "/";
        } else {
            hostAndPath[0] = this.bucket+"."+this.host;
            hostAndPath[1] = "/"+this.object;
        }
        return hostAndPath;
    }

    public TosRequest buildRequest(String method, InputStream stream) throws TosClientException {
        TosRequest request;
        try{
            request = build(method, stream);
        } catch (IOException e) {
            throw new TosClientException("build tos request failed", e);
        }
        if(signer != null){
            Map<String, String> signed = this.signer.signHeader(request);
            for (String key : signed.keySet()){
                request.getHeaders().put(key, signed.get(key));
            }
        }
        return request;
    }

    public TosRequest buildPreSignedUrlRequest(String method, long ttl) throws TosClientException {
        TosRequest request = genTosRequest(method, null);
        if (this.signer != null){
            Map<String, String> query = this.signer.signQuery(request, Duration.ofSeconds(ttl));
            if (request.getQuery() == null) {
                request.setQuery(new HashMap<>());
            }
            for (String key : query.keySet()){
                request.getQuery().put(key, query.get(key));
            }
        }
        return request;
    }

    public TosRequest buildRequestWithCopySource(String method, String srcBucket, String srcObject) throws TosClientException {
        TosRequest request;
        try{
            request = build(method, null);
        } catch (IOException e){
            throw new TosClientException("build tos request failed", e);
        }
        String versionID = null;
        if (request.getQuery() != null) {
            versionID = request.getQuery().get("versionId");
            request.getQuery().remove("versionId");
        }

        String cpSrcHeader = "";
        try{
            cpSrcHeader = copySource(srcBucket, srcObject, versionID);
        } catch (UnsupportedEncodingException e) {
            throw new TosClientException("object key encode exception", e);
        }
        request.getHeaders().put(TosHeader.HEADER_COPY_SOURCE, cpSrcHeader);
        if(signer != null){
            Map<String, String> signed = this.signer.signHeader(request);
            for (String key : signed.keySet()){
                request.getHeaders().put(key, signed.get(key));
            }
        }
        return request;
    }

    private String copySource(String bucket, String object, String versionID) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(versionID)) {
            return "/" + bucket + "/" + URLEncoder.encode(object, "UTF-8");
        }
        return "/" + bucket + "/" + URLEncoder.encode(object, "UTF-8") + "?versionId=" + versionID;
    }

    @Deprecated
    public String preSignedURL(String method, Duration ttl) throws TosClientException {
        TosRequest request;
        try{
            request = build(method, null);
        } catch (IOException e){
            throw new TosClientException("build tos request failed", e);
        }

        if (this.signer != null){
            Map<String, String> query = this.signer.signQuery(request, ttl);
            for (String key : query.keySet()){
                request.getQuery().put(key, query.get(key));
            }
        }
        return request.toURL().toString();
    }
}