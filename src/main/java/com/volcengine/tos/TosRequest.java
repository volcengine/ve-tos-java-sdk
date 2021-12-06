package com.volcengine.tos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class TosRequest {
    private String scheme;
    private String method;
    private String host;
    private String path;
    private long contentLength;
    @JsonIgnore
    private transient InputStream content;
    private Map<String, String> headers = Collections.emptyMap();
    private Map<String, String> query = Collections.emptyMap();

    // only for POST data
    private byte[] data = ArrayUtils.EMPTY_BYTE_ARRAY;

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
        this.content = inputStream;
        this.query = query;
        this.headers = headers;
    }

    public HttpUrl toURL() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        if (query != null) {
           query.forEach(builder::addQueryParameter);
        }
        return builder.scheme(scheme).host(host).encodedPath(path).build();
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

    public TosRequest setData(byte[] data) {
        this.data = data;
        return this;
    }
}
