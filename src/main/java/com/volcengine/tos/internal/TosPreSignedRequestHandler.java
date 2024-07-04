package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.auth.Credential;
import com.volcengine.tos.auth.SignKeyInfo;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.model.PostPolicyJson;
import com.volcengine.tos.internal.model.PreSignedPolicyJson;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.internal.util.base64.Base64;
import com.volcengine.tos.model.object.*;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TosPreSignedRequestHandler {
    private TosRequestFactory factory;
    private Signer signer;

    public TosPreSignedRequestHandler(TosRequestFactory factory, Signer signer) {
        ParamsChecker.ensureNotNull(factory, "TosRequestFactory");
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
        // 使用自定义域名时桶名不校验
        boolean useCustomDomain = factory.isCustomDomain();
        if (input.isCustomDomain() != null) {
            useCustomDomain = input.isCustomDomain();
        }
        if (!useCustomDomain) {
            ParamsChecker.isValidBucketName(input.getBucket());
        }
        ParamsChecker.isValidHttpMethod(input.getHttpMethod());
        long ttl = TosUtils.validateAndGetTtl(input.getExpires());

        String objectKey = input.getKey();
        if (StringUtils.isEmpty(objectKey)) {
            objectKey = "";
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), objectKey, input.getHeader());

        // 直接走 custom domain 模式，此设置针对单次请求，不会修改 factory 中的 urlMode
        builder.setUrlMode(useCustomDomain ? Consts.URL_MODE_CUSTOM_DOMAIN : Consts.URL_MODE_DEFAULT);
        if (StringUtils.isNotEmpty(input.getAlternativeEndpoint())) {
            String newHost= ParamsChecker.parseFromEndpoint(input.getAlternativeEndpoint()).get(1);
            builder.setHost(newHost);
        }

        if (input.getQuery() != null) {
            input.getQuery().forEach(builder::withQuery);
        }
        TosRequest request = this.factory.build(builder, input.getHttpMethod(), ttl);
        return new PreSignedURLOutput(request.toEscapeURL().toString(), request.getHeaders());
    }

    public PreSignedPostSignatureOutput preSignedPostSignature(PreSignedPostSignatureInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PreSignedPostSignatureInput");
        long ttl = TosUtils.validateAndGetTtl(input.getExpires());

        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        String date8601 = now.format(SigningUtils.iso8601Layout);
        String dateDay = now.format(SigningUtils.yyyyMMdd);

        String region = null;
        String ak = null;
        String sk = null;
        String securityToken = null;
        if (this.signer != null) {
            if (this.signer.getCredentialsProvider() != null) {
                com.volcengine.tos.credential.Credentials cred = this.signer.getCredentialsProvider().getCredentials();
                ak = cred.getAk();
                sk = cred.getSk();
                securityToken = cred.getSecurityToken();
            } else if (this.signer.getCredential() != null) {
                Credential cred = this.signer.getCredential().credential();
                ak = cred.getAccessKeyId();
                sk = cred.getAccessKeySecret();
                securityToken = cred.getSecurityToken();
            }
            region = this.signer.getRegion();
        }

        List<PostSignatureCondition> conditions = new ArrayList<>();
        // algorithm
        conditions.add(new PostSignatureCondition(SigningUtils.v4Algorithm, SigningUtils.signPrefix));
        // date
        conditions.add(new PostSignatureCondition(SigningUtils.v4Date, date8601));
        // credential
        String credential = null;
        if (ak != null && sk != null && region != null) {
            credential = String.format("%s/%s/%s/tos/request", ak, dateDay, region);
            conditions.add(new PostSignatureCondition(SigningUtils.v4Credential, credential));
            if (StringUtils.isNotEmpty(securityToken)) {
                conditions.add(new PostSignatureCondition(SigningUtils.v4SecurityToken, securityToken));
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
        String originPolicy = StringUtils.toString(new ByteArrayInputStream(marshalResult.getData()), "policyJson");

        // sign key
        byte[] signK = null;
        if (ak != null && sk != null && region != null) {
            signK = SigningUtils.signKey(new SignKeyInfo().setSk(sk).setDate(dateDay).setRegion(region));
        }

        // base64 encode policy
        byte[] policyInByte = Base64.encodeBase64(marshalResult.getData());
        String policy = StringUtils.toString(new ByteArrayInputStream(policyInByte), "policyInByte");

        // signature
        byte[] sign = SigningUtils.hmacSha256(signK, policyInByte);
        String signature = String.valueOf(SigningUtils.toHex(sign));

        return new PreSignedPostSignatureOutput().setOriginPolicy(originPolicy).setPolicy(policy).setAlgorithm(SigningUtils.signPrefix)
                .setCredential(credential).setDate(date8601).setSignature(signature);
    }

    public PreSignedPolicyURLOutput preSignedPolicyURL(PreSignedPolicyURLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PreSingedPolicyURLInput");
        ParamsChecker.isValidBucketName(input.getBucket());
        if (input.getConditions() == null || input.getConditions().size() == 0) {
            throw new TosClientException("empty PolicySignatureConditions", null);
        }
        for (PolicySignatureCondition condition : input.getConditions()) {
            if (!StringUtils.equals(condition.getKey(), SigningUtils.signConditionKey)) {
                throw new TosClientException("invalid pre signed url conditions, condition key should be 'key'", null);
            }
        }
        long ttl = TosUtils.validateAndGetTtl(input.getExpires());

        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        String date8601 = now.format(SigningUtils.iso8601Layout);
        String dateDay = now.format(SigningUtils.yyyyMMdd);

        List<Map.Entry<String, String>> query = new ArrayList<>();
        query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Algorithm, SigningUtils.signPrefix));
        query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Date, date8601));
        query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Expires, String.valueOf(ttl)));

        String region = null;
        String ak = null;
        String sk = null;
        String securityToken = null;
        if (this.signer != null) {
            if (this.signer.getCredentialsProvider() != null) {
                com.volcengine.tos.credential.Credentials cred = this.signer.getCredentialsProvider().getCredentials();
                ak = cred.getAk();
                sk = cred.getSk();
                securityToken = cred.getSecurityToken();
            } else if (this.signer.getCredential() != null) {
                Credential cred = this.signer.getCredential().credential();
                ak = cred.getAccessKeyId();
                sk = cred.getAccessKeySecret();
                securityToken = cred.getSecurityToken();
            }
            region = this.signer.getRegion();
        }
        // credential
        String credential = null;
        if (ak != null && sk != null && region != null) {
            credential = String.format("%s/%s/%s/tos/request", ak, dateDay, region);
            query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Credential, credential));
            if (StringUtils.isNotEmpty(securityToken)) {
                query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4SecurityToken, securityToken));
            }
        }
        List<PolicySignatureCondition> conditions = new ArrayList<>(input.getConditions());
        conditions.add(new PolicySignatureCondition().setKey(SigningUtils.signConditionBucket).setValue(input.getBucket()));
        PreSignedPolicyJson policyJson = new PreSignedPolicyJson().setConditions(conditions);
        // serialize
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(policyJson);
        // base64 encode policy
        byte[] policyInByte = Base64.encodeBase64(marshalResult.getData());
        String policy = StringUtils.toString(new ByteArrayInputStream(policyInByte), "policyInByte");
        query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Policy, policy));

        // CanonicalRequest
        final char split = '\n';
        String canonicalStr = SigningUtils.encodeQuery(query) + split + SigningUtils.unsignedPayload;
        byte[] sha256Value = SigningUtils.sha256(canonicalStr);
        String hexValue = String.valueOf(SigningUtils.toHex(sha256Value));
        String buf = SigningUtils.signPrefix + split +
                date8601 + split +
                dateDay + '/' + region + "/tos/request" + split +
                hexValue;

        // sign key
        byte[] signK = null;
        if (ak != null && sk != null && region != null) {
            signK = SigningUtils.signKey(new SignKeyInfo().setSk(sk).setDate(dateDay).setRegion(region));
        }
        byte[] sign = SigningUtils.hmacSha256(signK, buf.getBytes());

        query.add(new AbstractMap.SimpleEntry<>(SigningUtils.v4Signature, String.valueOf(SigningUtils.toHex(sign))));
        String encodedQuery = SigningUtils.encodeQuery(query);

        String scheme = factory.getScheme();
        String host = factory.getHost();
        if (StringUtils.isNotEmpty(input.getAlternativeEndpoint())) {
            host = ParamsChecker.parseFromEndpoint(input.getAlternativeEndpoint()).get(1);
        }

        PreSignedPolicyURLGenerator generator = new DefaultPreSignedPolicyURLGenerator().setScheme(scheme)
                .setSignatureQuery(encodedQuery).setCustomDomain(input.isCustomDomain())
                .setBucket(input.getBucket()).setHost(host);

        return new PreSignedPolicyURLOutput().setPreSignedPolicyURLGenerator(generator)
                .setScheme(scheme).setHost(host).setSignatureQuery(encodedQuery);
    }
}
