package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class FollowRedirectTest {

    private static final String endpoint = Consts.endpoint;
    private static final String region = Consts.region;
    private static final String accessKey = Consts.accessKey;
    private static final String secretKey = Consts.secretKey;
    private static final String bucketName = Consts.bucket;
    @Test
    public void redirectTest() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setResponseCode(302)
                    .addHeader("Location", "http://localhost:" + server.getPort() + "/redirected_0"));
            server.enqueue(new MockResponse().setResponseCode(301)
                    .addHeader("Location", "http://localhost:" + server.getPort() + "/redirected_1"));
            server.enqueue(new MockResponse().setResponseCode(200));

            TransportConfig config = TransportConfig.builder().maxRetryCount(3).readTimeoutMills(1000)
                    .writeTimeoutMills(1000).except100ContinueThreshold(0).build();
            Transport transport = new RequestTransport(config);

            TosRequest tosRequest = new TosRequest("http", "GET", server.getHostName(), "")
                    .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true)
                    .setFollowRedirectTimes(3);
            TosResponse response = transport.roundTrip(tosRequest);

            // 验证响应
            Assert.assertEquals(response.getStatusCode(), 200);

            // 验证请求顺序
            RecordedRequest firstRequest = server.takeRequest();
            Assert.assertEquals(firstRequest.getPath(), "/");

            RecordedRequest secondRequest = server.takeRequest();
            Assert.assertEquals(secondRequest.getPath(), "/redirected_0");

            RecordedRequest thirdRequest = server.takeRequest();
            Assert.assertEquals(thirdRequest.getPath(), "/redirected_1");

        }catch (Throwable t){
            Assert.fail(t.getMessage());
        }
    }
}
