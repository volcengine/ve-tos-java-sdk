package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;

import java.time.Duration;
import java.util.Map;

public class TosPreSignedRequestHandler {
    private static final long MAX_TTL = 604800;
    private static final long DEFAULT_TTL = 3600;
    private TosRequestFactory factory;
    private Signer signer;

    public TosPreSignedRequestHandler(TosRequestFactory factory, Signer signer) {
        this.factory = factory;
        this.signer = signer;
    }

    public TosRequestFactory getFactory() {
        return factory;
    }

    public TosPreSignedRequestHandler setFactory(TosRequestFactory factory) {
        this.factory = factory;
        return this;
    }

    public Signer getSigner() {
        return signer;
    }

    public TosPreSignedRequestHandler setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public PreSignedURLOutput preSignedURL(PreSignedURLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PreSignedURLInput");
        ParamsChecker.isValidBucketName(input.getBucket());
        ParamsChecker.isValidHttpMethod(input.getHttpMethod());
        long ttl = input.getExpires();
        if (input.getExpires() <= 0) {
            ttl = DEFAULT_TTL;
        }
        if (input.getExpires() > MAX_TTL) {
            throw new TosClientException("tos: invalid preSignedUrl expires, should not be larger than 604800 seconds.", null);
        }

        String objectKey = input.getKey();
        if (StringUtils.isEmpty(objectKey)) {
            objectKey = "";
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), objectKey, input.getHeader());

        if (StringUtils.isNotEmpty(input.getAlternativeEndpoint())) {
            String newHost= ParamsChecker.parseFromEndpoint(input.getAlternativeEndpoint()).get(1);
            builder.setHost(newHost);
        }

        if (input.getQuery() != null) {
            input.getQuery().forEach(builder::withQuery);
        }
        TosRequest request = this.factory.build(builder, input.getHttpMethod(), null);

        if (this.signer != null) {
            Map<String, String> query = this.signer.signQuery(request, Duration.ofSeconds(ttl));
            if (request.getQuery() == null) {
                request.setQuery(query);
            } else {
                request.getQuery().putAll(query);
            }
        }
        return new PreSignedURLOutput(request.toURL().toString(), request.getHeaders());
    }
}
