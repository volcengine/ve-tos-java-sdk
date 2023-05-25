package com.volcengine.tos;

import com.volcengine.tos.auth.StaticCredentials;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TOSClientBuilderTest {
    @Test
    void isCustomDomainTest() {
        TOSV2Client client = new TOSV2Client(TOSClientConfiguration.builder()
                .isCustomDomain(true)
                .endpoint(Consts.endpoint)
                .region(Consts.region)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());
        Assert.assertTrue(client.getFactory().isCustomDomain());
        Assert.assertEquals(client.getFactory().getUrlMode(), com.volcengine.tos.internal.Consts.URL_MODE_PATH);
    }
}
