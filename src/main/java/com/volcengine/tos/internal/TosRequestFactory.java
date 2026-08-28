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
    private String controlScheme;
    private String controlHost;
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

    private void parseControlEndpoint(String controlEndpoint) {
        List<String> schemeAndHost = ParamsChecker.parseFromEndpoint(controlEndpoint);
        this.controlScheme = schemeAndHost.get(0);
        this.controlHost = schemeAndHost.get(1);
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

    public TosRequestFactory setControlEndpoint(String controlEndpoint) {
        if (StringUtils.isNotEmpty(controlEndpoint)) {
            parseControlEndpoint(controlEndpoint);
        }

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

    public String getControlScheme() {
        return controlScheme;
    }

    public TosRequestFactory setControlScheme(String controlScheme) {
        this.controlScheme = controlScheme;
        return this;
    }

    public String getControlHost() {
        return controlHost;
    }

    public TosRequestFactory setControlHost(String controlHost) {
        this.controlHost = controlHost;
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

    public RequestBuilder initVectorReq(String accountId, String bucket, String tableApiPath, Map<String, String> headers) {
        return newVectorReqBuilder(accountId, bucket, tableApiPath, headers).setUrlMode(URL_MODE_CONTROL_DOMAIN).setPort(port);
    }

    public RequestBuilder initControlReq(String accountId, String controlApiPath, Map<String, String> headers) {
        return newControlReqBuilder(accountId, controlApiPath, headers).setUrlMode(URL_MODE_CONTROL_DOMAIN).setPort(port);
    }

    public TosRequest build(RequestBuilder builder, String method, InputStream content) {
        return builder.buildRequest(method, content);
    }

    public TosRequest build(RequestBuilder builder, String method, boolean isSignedAllHeaders, long ttl) {
        return builder.buildPreSignedUrlRequest(method, isSignedAllHeaders, ttl);
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

    private RequestBuilder newVectorReqBuilder(String accountId, String bucket, String vectorApiPath, Map<String, String> headers) {
        String vectorHost;
        if (StringUtils.isNotEmpty(accountId) && StringUtils.isNotEmpty(bucket)) {
            vectorHost = bucket + "-" + accountId + "." + this.host;
        } else  {
            vectorHost = this.host;
        };
        RequestBuilder rb = new RequestBuilder(vectorApiPath, this.scheme, vectorHost, this.signer);
        if (StringUtils.isEmpty(this.userAgent)) {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, TosUtils.getUserAgent());
        } else {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, this.userAgent);
        }
        if (headers != null) {
            headers.forEach(rb::withHeader);
        }
        return rb;
    }

    private RequestBuilder newControlReqBuilder(String accountId, String controlApiPath, Map<String, String> headers) {
        String controlHost = accountId + "." + this.controlHost;
        RequestBuilder rb = new RequestBuilder(controlApiPath, this.controlScheme, controlHost, this.signer)
                .setDisableEncodingMeta(this.disableEncodingMeta);
        if (StringUtils.isEmpty(this.userAgent)) {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, TosUtils.getUserAgent());
        } else {
            rb.withHeader(TosHeader.HEADER_USER_AGENT, this.userAgent);
        }
        rb.withHeader(TosHeader.HEADER_ACCOUNT_ID, accountId);

        if (headers != null) {
            headers.forEach(rb::withHeader);
        }
        return rb;

    }
}
