package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.model.HttpRange;
import com.volcengine.tos.internal.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.volcengine.tos.internal.Consts.URL_MODE_DEFAULT;
import static com.volcengine.tos.internal.Consts.URL_MODE_PATH;

public class RequestBuilder {
    private Signer signer;
    private String scheme;
    private String host;
    private int port;
    private String bucket;
    private String object;
    private int urlMode = URL_MODE_DEFAULT;
    private long contentLength;
    private HttpRange range;
    private Map<String, String> headers = new HashMap<>(1);
    private Map<String, String> query;
    private boolean autoRecognizeContentType = true;
    private String preHashCrc64ecma;

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

    private TosRequest build(String method, InputStream stream) throws IOException {
        TosRequest request = genTosRequest(method, stream);
        if (stream != null){
            if (this.contentLength > 0) {
                request.setContentLength(contentLength);
            } else if (StringUtils.isNotEmpty(headers.get(TosHeader.HEADER_CONTENT_LENGTH))) {
                try{
                    long cl = Long.parseLong(headers.get(TosHeader.HEADER_CONTENT_LENGTH));
                    request.setContentLength(cl > 0 ? cl : -1L);
                } catch (NumberFormatException e) {
                    request.setContentLength(-1L);
                }
            } else {
                request.setContentLength(-1L);
            }
        }
        return request;
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
        String[] res = new String[]{this.host, ""};
        if (StringUtils.isEmpty(this.bucket)){
            res[1] = "/";
            return res;
        }
        if (urlMode == URL_MODE_PATH) {
            res[1] = "/" + this.bucket + "/" + this.object;
            return res;
        }
        res[0] = this.bucket+"."+this.host;
        res[1] = "/"+this.object;
        return res;
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