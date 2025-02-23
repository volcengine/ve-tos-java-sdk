package com.volcengine.tos.internal;

import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.volcengine.tos.internal.Consts.*;

public class TosRequestFactory {
    private Signer signer;
    private String scheme;
    private String host;
    private int port;
    private int urlMode = URL_MODE_DEFAULT;
    private boolean isCustomDomain;
    private boolean disableEncodingMeta;
    private String userAgent;

    public TosRequestFactory(Signer signer, String endpoint) {
        this.signer = signer;
        parseEndpoint(endpoint);
    }

    private void parseEndpoint(String endpoint) {
        List<String> schemeAndHost = ParamsChecker.parseFromEndpoint(endpoint);
        this.scheme = schemeAndHost.get(0);
        this.host = schemeAndHost.get(1);
        if (ParamsChecker.isLocalhostOrIpAddress(this.host)) {
            this.urlMode = URL_MODE_PATH;
            // get port from ip
            this.port = ParamsChecker.parsePort(this.host);
            if (port != 0) {
                int lastIdx = this.host.length() - String.valueOf(port).length() - 1;
                this.host = host.substring(0, lastIdx);
            }
            // if port == 0, use the whole host
        }
    }

    public Signer getSigner() {
        return signer;
    }

    public TosRequestFactory setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public TosRequestFactory setEndpoint(String endpoint) {
        parseEndpoint(endpoint);
        return this;
    }

    public int getUrlMode() {
        return urlMode;
    }

    public TosRequestFactory setUrlMode(int urlMode) {
        this.urlMode = urlMode;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public TosRequestFactory setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TosRequestFactory setHost(String host) {
        this.host = host;
        return this;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public TosRequestFactory setIsCustomDomain(boolean isCustomDomain) {
        this.isCustomDomain = isCustomDomain;
        if (this.isCustomDomain) {
            this.urlMode = URL_MODE_CUSTOM_DOMAIN;
        }
        return this;
    }

    public TosRequestFactory setDisableEncodingMeta(boolean disableEncodingMeta) {
        this.disableEncodingMeta = disableEncodingMeta;
        return this;
    }

    public TosRequestFactory setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public RequestBuilder init(String bucket, String object, Map<String, String> headers) {
        return newBuilder(bucket, object, headers).setUrlMode(urlMode).setPort(port);
    }

    public TosRequest build(RequestBuilder builder, String method, InputStream content) {
        return builder.buildRequest(method, content);
    }

    public TosRequest build(RequestBuilder builder, String method, long ttl) {
        return builder.buildPreSignedUrlRequest(method, ttl);
    }

    public TosRequest buildWithCopy(RequestBuilder builder, String method, String srcBucket, String srcObject) {
        return builder.buildRequestWithCopySource(method, srcBucket, srcObject);
    }

    private RequestBuilder newBuilder(String bucket, String object) {
        RequestBuilder rb = new RequestBuilder(bucket, object, this.scheme, this.host, this.signer).setDisableEncodingMeta(this.disableEncodingMeta);
        if (StringUtils.isEmpty(this.userAgent)) {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, TosUtils.getUserAgent());
        } else {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, this.userAgent);
        }
        return rb;
    }

    private RequestBuilder newBuilder(String bucket, String object, Map<String, String> headers) {
        RequestBuilder rb = newBuilder(bucket, object);
        if (headers != null) {
            headers.forEach(rb::withHeader);
        }
        return rb;
    }
}
