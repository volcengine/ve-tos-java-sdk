package com.volcengine.tos.internal;

import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TosRequestFactoryImpl implements TosRequestFactory {
    private Signer signer;
    private String scheme;
    private String host;

    public TosRequestFactoryImpl(Signer signer, String endpoint) {
        this.signer = signer;
        List<String> schemeAndHost = ParamsChecker.parseFromEndpoint(endpoint);
        this.scheme = schemeAndHost.get(0);
        this.host = schemeAndHost.get(1);
    }

    @Override
    public RequestBuilder init(String bucket, String object, Map<String, String> headers) {
        return newBuilder(bucket, object, headers);
    }

    @Override
    public TosRequest build(RequestBuilder builder, String method, InputStream content) {
        return builder.buildRequest(method, content);
    }

    @Override
    public TosRequest buildWithCopy(RequestBuilder builder, String method, String srcBucket, String srcObject) {
        return builder.buildRequestWithCopySource(method, srcBucket, srcObject);
    }

    private RequestBuilder newBuilder(String bucket, String object) {
        RequestBuilder rb = new RequestBuilder(bucket, object, this.scheme, this.host, this.signer);
        rb.withHeader(TosHeader.HEADER_USER_AGENT, TosUtils.getUserAgent());
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
