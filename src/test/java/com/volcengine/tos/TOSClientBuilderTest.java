package com.volcengine.tos;

import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.internal.RequestBuilder;
import com.volcengine.tos.internal.TosRequest;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import okhttp3.HttpUrl;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TOSClientBuilderTest {
    @Test
    void isCustomDomainTest() {
        TOSClientConfiguration configuration = TOSClientConfiguration.builder()
                .isCustomDomain(true)
                .endpoint(Consts.endpoint)
                .region(Consts.region)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build();
        TOSV2Client client = new TOSV2Client(configuration);
        Assert.assertTrue(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_CUSTOM_DOMAIN);
        String bucket = "aaa";
        String key = "bbb";
        RequestBuilder builder = client.getFactory().init(bucket, key, null);
        TosRequest req = builder.buildRequest("GET", null);
        HttpUrl url = req.toURL();
        String endpoint = Consts.endpoint;
        if (!endpoint.startsWith("http") && !endpoint.startsWith("https")) {
            endpoint = "https://" + endpoint;
        }
        Assert.assertEquals(url.toString(), endpoint+"/"+key);

        // 分别测试以下6种场景
        // input customDomain null, client customDomain false
        // input customDomain false, client customDomain false
        // input customDomain true, client customDomain false
        // input customDomain null, client customDomain true
        // input customDomain false, client customDomain true
        // input customDomain true, client customDomain true

        // client customDomain true
        endpoint = Consts.endpoint2;
        if (!endpoint.startsWith("http") && !endpoint.startsWith("https")) {
            endpoint = "https://" + endpoint;
        }
        // input customDomain null
        PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2);
        PreSignedURLOutput output = client.preSignedURL(input);
        Assert.assertTrue(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertTrue(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_CUSTOM_DOMAIN);
        // input customDomain false
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2).setCustomDomain(false);
        try{
            output = client.preSignedURL(input);
            Assert.fail();
        } catch (TosClientException e) {
        }
        input.setBucket(Consts.bucket);
        output = client.preSignedURL(input);
        Assert.assertFalse(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertTrue(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_CUSTOM_DOMAIN);
        // input customDomain true
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2).setCustomDomain(true);
        output = client.preSignedURL(input);
        Assert.assertTrue(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertTrue(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_CUSTOM_DOMAIN);


        configuration.setCustomDomain(false);
        client = new TOSV2Client(configuration);
        Assert.assertFalse(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_DEFAULT);

        // client customDomain false
        endpoint = Consts.endpoint2;
        if (!endpoint.startsWith("http") && !endpoint.startsWith("https")) {
            endpoint = "https://" + endpoint;
        }
        // input customDomain null
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2);
        try{
            output = client.preSignedURL(input);
            Assert.fail();
        } catch (TosClientException e) {
        }
        input.setBucket(Consts.bucket);
        output = client.preSignedURL(input);
        Assert.assertFalse(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertFalse(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_DEFAULT);
        // input customDomain false
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2).setCustomDomain(false);
        try{
            output = client.preSignedURL(input);
            Assert.fail();
        } catch (TosClientException e) {
        }
        input.setBucket(Consts.bucket);
        output = client.preSignedURL(input);
        Assert.assertFalse(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertFalse(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_DEFAULT);
        // input customDomain true
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2).setCustomDomain(true);
        output = client.preSignedURL(input);
        Assert.assertTrue(output.getSignedUrl().startsWith(endpoint+"/"+key));
        Assert.assertFalse(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_DEFAULT);
    }
}
