package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.credential.CredentialsProvider;
import com.volcengine.tos.transport.TransportConfig;

public interface TOSV2Builder {
    /**
     * use the specific TOS region, endpoint, accessKeyId and secretKeyId
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     */
    TOSV2 build(String region, String endpoint, String accessKey, String secretKey);

    /**
     * use the specific TOS region, endpoint, accessKeyId, secretKeyId and security token from STS
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     * @param securityToken temporally access security token from STS.
     */
    TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken);

    /**
     * use the specific TOS region, endpoint, customer credentials.
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param credentials implement the Credentials interface to provide accessKey, secretKey and securityToken(optional).
     */
    TOSV2 build(String region, String endpoint, Credentials credentials);

    TOSV2 build(String region, String endpoint, CredentialsProvider credentialsProvider);

    /**
     * use the specific TOS region, endpoint, accessKeyId, secretKeyId and customer client configuration
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     * @param conf Customer http request configuration.
     */
    TOSV2 build(String region, String endpoint, String accessKey, String secretKey, TransportConfig conf);

    /**
     * use the specific TOS region, endpoint, customer credentials and customer http request configuration
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param credentials implement the Credentials interface to provide accessKey, secretKey and securityToken(optional).
     * @param conf Customer http request configuration.
     * @return
     */
    TOSV2 build(String region, String endpoint, Credentials credentials, TransportConfig conf);

    TOSV2 build(String region, String endpoint, CredentialsProvider credentialsProvider, TransportConfig conf);

    /**
     * use the specific TOS region, endpoint, accessKeyId, secretKeyId and security token from STS
     * to create a new client instance.
     * @param region The specific service region, such as "cn-beijing".
     * @param endpoint The specific service endpoint, such as "https://tos-cn-beijing.volces.com".
     * @param accessKey Your account's access key.
     * @param secretKey Your account's secret key.
     * @param securityToken temporally access security token from STS.
     * @param conf Customer http request configuration.
     */
    TOSV2 build(String region, String endpoint, String accessKey, String secretKey, String securityToken, TransportConfig conf);

    /**
     * use the specific TOS region, endpoint, customer credentials and customer client configuration.
     * to create a new client instance.
     * @param conf Customer client configuration.
     */
    TOSV2 build(TOSClientConfiguration conf);
}
