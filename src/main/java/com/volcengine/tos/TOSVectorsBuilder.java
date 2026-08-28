package com.volcengine.tos;

public interface TOSVectorsBuilder {
    /**
     * use the specific TOS region, endpoint, accessKeyId and secretKeyId
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tosvectors-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     */
    TOSVectors build(String region, String endpoint, String accessKey, String secretKey);
    /**
     * use the specific TOS region, endpoint, accessKeyId, secretKeyId and security token from STS
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tosvectors-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     * @param securityToken temporally access security token from STS.
     */
    TOSVectors build(String region, String endpoint, String accessKey, String secretKey, String securityToken);
    TOSVectors build(TOSVectorsClientConfiguration conf);
}
