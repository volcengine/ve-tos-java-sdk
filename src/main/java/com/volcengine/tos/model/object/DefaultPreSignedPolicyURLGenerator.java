package com.volcengine.tos.model.object;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import okhttp3.HttpUrl;

import java.util.Map;

public class DefaultPreSignedPolicyURLGenerator implements PreSignedPolicyURLGenerator {
    private String signatureQuery;
    private String host;
    private String scheme;
    private String bucket;
    private boolean isCustomDomain;

    public String getSignatureQuery() {
        return signatureQuery;
    }

    public DefaultPreSignedPolicyURLGenerator setSignatureQuery(String signatureQuery) {
        this.signatureQuery = signatureQuery;
        return this;
    }

    public String getHost() {
        return host;
    }

    public DefaultPreSignedPolicyURLGenerator setHost(String host) {
        this.host = host;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public DefaultPreSignedPolicyURLGenerator setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public DefaultPreSignedPolicyURLGenerator setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public DefaultPreSignedPolicyURLGenerator setCustomDomain(boolean customDomain) {
        this.isCustomDomain = customDomain;
        return this;
    }

    @Override
    public String getSignedURLForList(Map<String, String> additionalQuery) {
        return generateUrl(null, additionalQuery);
    }

    @Override
    public String getSignedURLForGetOrHead(String key, Map<String, String> additionalQuery) {
        if (StringUtils.isEmpty(key)) {
            throw new TosClientException("empty key", null);
        }
        return generateUrl(key, additionalQuery);
    }

    private String generateUrl(String key, Map<String, String> additionalQuery) {
        HttpUrl.Builder url = new HttpUrl.Builder();
        String host = null;
        if (!isCustomDomain) {
            ParamsChecker.isValidBucketName(bucket);
            host = bucket + "." + this.host;
        } else {
            host = this.host;
        }
        String path = StringUtils.isNotEmpty(key) ? key : "";
        url = url.scheme(scheme).host(host).addPathSegment(path).encodedQuery(signatureQuery);
        if (additionalQuery != null) {
            for (Map.Entry<String, String> entry : additionalQuery.entrySet()) {
                url.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return url.build().toString();
    }

    @Override
    public String toString() {
        return "DefaultPreSignedPolicyURLGenerator{" +
                "signatureQuery='" + signatureQuery + '\'' +
                ", host='" + host + '\'' +
                ", scheme='" + scheme + '\'' +
                ", bucket='" + bucket + '\'' +
                ", isCustomDomain=" + isCustomDomain +
                '}';
    }
}
