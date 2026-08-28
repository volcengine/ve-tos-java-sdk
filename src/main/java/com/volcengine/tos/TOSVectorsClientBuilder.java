package com.volcengine.tos;

import com.volcengine.tos.credential.CredentialsProvider;
import com.volcengine.tos.credential.StaticCredentialsProvider;
import com.volcengine.tos.internal.util.StringUtils;

public class TOSVectorsClientBuilder implements TOSVectorsBuilder {
    public TOSVectorsClientBuilder() {}

    @Override
    public TOSVectors build(String region, String endpoint, String accessKey, String secretKey) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey);
        }
        TOSVectorsClientConfiguration conf = TOSVectorsClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).build();
        return build(conf);
    }

    @Override
    public TOSVectors build(String region, String endpoint, String accessKey, String secretKey, String securityToken) {
        CredentialsProvider cred = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey) && StringUtils.isNotEmpty(securityToken)) {
            cred = new StaticCredentialsProvider(accessKey, secretKey, securityToken);
        }
        TOSVectorsClientConfiguration clientConfiguration = TOSVectorsClientConfiguration.builder()
                .region(region).endpoint(endpoint).credentialsProvider(cred).build();
        return build(clientConfiguration);
    }

    @Override
    public TOSVectors build(TOSVectorsClientConfiguration conf) {
        return new TOSVectorsClient(conf);
    }
}
