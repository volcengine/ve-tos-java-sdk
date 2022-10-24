package com.volcengine.tos.internal;

import com.volcengine.tos.transport.TransportConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class RetryTest {
    @Test
    void retryTest() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500).setHeader("custom", "volc"));
        server.start();
        TransportConfig config = TransportConfig.builder().maxRetryCount(3).readTimeoutMills(1000)
                .writeTimeoutMills(1000).build();
        Transport transport = new RequestTransport(config);

        // 500 retry
        TosRequest tosRequest = new TosRequest("http", "GET", server.getHostName(), "")
                .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true);
        TosResponse response = transport.roundTrip(tosRequest);
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.getHeaders().get("custom"), "volc");
        Assert.assertEquals(server.getRequestCount(), 4);
        RecordedRequest request = server.takeRequest();
        Assert.assertEquals(request.getMethod(), "GET");

        // 429 retry
        server.enqueue(new MockResponse().setResponseCode(429));
        server.enqueue(new MockResponse().setResponseCode(429));
        server.enqueue(new MockResponse().setResponseCode(429));
        server.enqueue(new MockResponse().setResponseCode(429).setHeader("custom", "volc_2"));
        tosRequest = new TosRequest("http", "GET", server.getHostName(), "")
                .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true);
        response = transport.roundTrip(tosRequest);
        Assert.assertEquals(response.getStatusCode(), 429);
        Assert.assertEquals(response.getHeaders().get("custom"), "volc_2");
        Assert.assertEquals(server.getRequestCount(), 8);

        // client exception
        tosRequest = new TosRequest("http", "GET", server.getHostName(), "/invalid/path")
                .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true);
        long start = System.currentTimeMillis();
        long end = 0;
        try{
            transport.roundTrip(tosRequest);
        } catch (IOException e) {
            end = System.currentTimeMillis();
            Assert.assertTrue(e instanceof SocketTimeoutException);
            Assert.assertTrue((end - start) / 1000 > 3);
        }
        server.shutdown();
        // connect exception
        tosRequest = new TosRequest("http", "GET", server.getHostName(), "/invalid/path")
                .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true);
        try{
            transport.roundTrip(tosRequest);
        } catch (IOException e) {
            Assert.assertTrue(e instanceof ConnectException);
        }
    }
}
