package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.auth.Credential;
import com.volcengine.tos.auth.SignKeyInfo;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.model.PostPolicyJson;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.object.*;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TosPreSignedRequestHandler {
    private TosRequestFactory factory;
    private Signer signer;

    public TosPreSignedRequestHandler(TosRequestFactory factory, Signer signer) {
        ParamsChecker.ensureNotNull(factory, "TosRequestFactory");
        ParamsChecker.ensureNotNull(signer, "Signer");
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
        long ttl = TosUtils.validateAndGetTtl(input.getExpires());

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

    public PreSignedPostSignatureOutput preSignedPostSignature(PreSignedPostSignatureInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PreSignedPostSignatureInput");
        long ttl = TosUtils.validateAndGetTtl(input.getExpires());

        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        String date8601 = now.format(SigningUtils.iso8601Layout);
        String dateDay = now.format(SigningUtils.yyyyMMdd);

        String region = null;
        Credential cred = null;
        if (this.signer != null && this.signer.getCredential() != null) {
            cred = this.signer.getCredential().credential();
            region = this.signer.getRegion();
        }

        List<PostSignatureCondition> conditions = new ArrayList<>();
        // algorithm
        conditions.add(new PostSignatureCondition(SigningUtils.v4Algorithm, SigningUtils.signPrefix));
        // date
        conditions.add(new PostSignatureCondition(SigningUtils.v4Date, date8601));
        // credential
        String credential = null;
        if (cred != null && region != null) {
            credential = String.format("%s/%s/%s/tos/request", cred.getAccessKeyId(), dateDay, region);
            conditions.add(new PostSignatureCondition(SigningUtils.v4Credential, credential));
            if (StringUtils.isNotEmpty(cred.getSecurityToken())) {
                conditions.add(new PostSignatureCondition(SigningUtils.v4SecurityToken, cred.getSecurityToken()));
            }
        }
        // bucket
        if (StringUtils.isNotEmpty(input.getBucket())) {
            ParamsChecker.isValidBucketName(input.getBucket());
            conditions.add(new PostSignatureCondition(SigningUtils.signConditionBucket, input.getBucket()));
        }
        // key
        if (StringUtils.isNotEmpty(input.getKey())) {
            ParamsChecker.isValidKey(input.getKey());
            conditions.add(new PostSignatureCondition(SigningUtils.signConditionKey, input.getKey()));
        }
        // custom conditions
        if (input.getConditions() != null) {
            for (int i = 0; i < input.getConditions().size(); i++) {
                PostSignatureCondition condition = input.getConditions().get(i);
                if (condition.getOperator() != null) {
                    conditions.add(new PostSignatureCondition("$"+condition.getKey(), condition.getValue(), condition.getOperator()));
                } else {
                    conditions.add(new PostSignatureCondition(condition.getKey(), condition.getValue()));
                }
            }
        }
        // custom content range
        if (input.getContentLengthRange() != null) {
            String key = String.valueOf(input.getContentLengthRange().getRangeStart());
            String value = String.valueOf(input.getContentLengthRange().getRangeEnd());
            conditions.add(new PostSignatureCondition(key, value, SigningUtils.signConditionRange));
        }

        // expiration
        String expiration = now.plusSeconds(ttl).format(SigningUtils.serverTimeFormat);

        PostPolicyJson policyJson = new PostPolicyJson().setConditions(conditions).setExpiration(expiration);
        // serialize
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(policyJson);
        String originPolicy = null;
        try {
            originPolicy = StringUtils.toString(new ByteArrayInputStream(marshalResult.getData()));
        } catch (IOException e) {
            throw new TosClientException("[preSignedPostSignature] read data toString failed, data is ." + Arrays.toString(marshalResult.getData()), e);
        }

        // sign key
        byte[] signK = null;
        if (cred != null && region != null) {
            signK = SigningUtils.signKey(new SignKeyInfo().setCredential(cred).setDate(dateDay).setRegion(region));
        }

        // base64 encode policy
        byte[] policyInByte = Base64.encodeBase64(marshalResult.getData());
        String policy = null;
        try {
            policy = StringUtils.toString(new ByteArrayInputStream(policyInByte));
        } catch (IOException e) {
            throw new TosClientException("[preSignedPostSignature] read data toString failed, data is ." + Arrays.toString(policyInByte), e);
        }

        // signature
        byte[] sign = SigningUtils.hmacSha256(signK, policyInByte);
        String signature = String.valueOf(SigningUtils.toHex(sign));

        return new PreSignedPostSignatureOutput().setOriginPolicy(originPolicy).setPolicy(policy).setAlgorithm(SigningUtils.signPrefix)
                .setCredential(credential).setDate(date8601).setSignature(signature);
    }
}