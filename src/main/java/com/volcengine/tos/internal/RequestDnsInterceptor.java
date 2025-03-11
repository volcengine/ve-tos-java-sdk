package com.volcengine.tos.internal;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hc.client5.http.classic.ExecChain;
import org.apache.hc.client5.http.classic.ExecChain.Scope;
import org.apache.hc.client5.http.classic.ExecChainHandler;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;

import com.volcengine.tos.internal.util.dnscache.DnsCacheService;

public class RequestDnsInterceptor implements ExecChainHandler {

    private DnsCacheService dnsCacheService;

    RequestDnsInterceptor(DnsCacheService dnsCacheService) {
        this.dnsCacheService = dnsCacheService;
    }

    @Override
    public ClassicHttpResponse execute(ClassicHttpRequest request, Scope scope, ExecChain chain)
            throws IOException, HttpException {
        try {
            return chain.proceed(request, scope);
        } catch (IOException | HttpException e) {
            try {
                if (request != null && request.getUri() != null && request.getUri().getHost() != null) {
                    this.dnsCacheService.removeAddress(request.getUri().getHost());
                }
            } catch (URISyntaxException ue) {
            }
            throw e;
        }
    }

    public void setDnsCacheService(DnsCacheService dnsCacheService) {
        this.dnsCacheService = dnsCacheService;
    }

}
