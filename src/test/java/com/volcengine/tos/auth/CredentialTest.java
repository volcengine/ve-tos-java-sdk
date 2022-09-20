package com.volcengine.tos.auth;

import com.volcengine.tos.ClientOptions;
import com.volcengine.tos.Consts;
import com.volcengine.tos.TOSClient;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;


public class CredentialTest {
    @Test
    void federationCredentialsTest() throws TosException, InterruptedException {
        final int[] called = {0};
        MockFederationTokenProvider tokenProvider = new MockFederationTokenProvider()
                .setProvider(() -> {
                    called[0]++;
                    return new FederationToken().setCredential(new Credential(Consts.accessKey, Consts.secretKey, ""))
                            .setExpiration(LocalDateTime.now().plusMinutes(10));
                });
        FederationCredentials tokenCredential = new FederationCredentials(tokenProvider);
        Assert.assertEquals(1, called[0]);
        TOSClient client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
                ClientOptions.withCredentials(tokenCredential));
        Assert.assertNotNull(client);
        String notFoundBucket = "notfoundbucket";
        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(Code.NO_SUCH_BUCKET, e.getCode());
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
        Thread.sleep(1000);
        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(Code.NO_SUCH_BUCKET, e.getCode());
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        called[0] = 0;
        tokenProvider = new MockFederationTokenProvider()
                .setProvider(() -> {
                    called[0]++;
                    return new FederationToken().setCredential(new Credential(Consts.accessKey, Consts.secretKey, ""))
                            .setExpiration(LocalDateTime.now().plusSeconds(2));
                });
        tokenCredential = new FederationCredentials(tokenProvider).withPreFetch(Duration.ofSeconds(1));
        Assert.assertEquals(1, called[0]);
        client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
                ClientOptions.withCredentials(tokenCredential));

        Assert.assertNotNull(client);
        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(Code.NO_SUCH_BUCKET, e.getCode());
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
        Assert.assertEquals(1, called[0]);

        Thread.sleep(1100);

        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(Code.NO_SUCH_BUCKET, e.getCode());
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
        Assert.assertEquals(2, called[0]);

        Thread.sleep(3000);

        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(Code.NO_SUCH_BUCKET, e.getCode());
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
        Assert.assertEquals(3, called[0]);
    }
}

@FunctionalInterface
interface Provider{
    FederationToken provider();
}
class MockFederationTokenProvider implements FederationTokenProvider {

    private Provider provider;
    @Override
    public FederationToken federationToken(){
        return provider.provider();
    }

    public MockFederationTokenProvider setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }
}
