package com.volcengine.tos.internal;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.zip.CheckedInputStream;

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
                .writeTimeoutMills(1000).except100ContinueThreshold(0).build();
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

        server.enqueue(new MockResponse().setHeadersDelay(3, TimeUnit.SECONDS).setBody("helloworld"));

        // client exception
        tosRequest = new TosRequest("http", "GET", server.getHostName(), "/invalid/path")
                .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true);
        long start = System.currentTimeMillis();
        long end = 0;
        try{
            transport.roundTrip(tosRequest);
            Assert.assertTrue(false);
        } catch (IOException e) {
            end = System.currentTimeMillis();
            Assert.assertTrue(e instanceof SocketTimeoutException || e instanceof ConnectException);
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

    @Test
    void retryWithStreamTest() throws IOException, InterruptedException {
        String bodyStr = StringUtils.randomString(1024*1024);
        String filePath = "src/test/resources/tmp."+System.nanoTime();
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(bodyStr.getBytes());
            fos.write(bodyStr.getBytes());
            fos.flush();
        }
        CRC64Checksum checksumTmp = new CRC64Checksum(0);
        CheckedInputStream inputStream = new CheckedInputStream(new FileInputStream(filePath), checksumTmp);
        byte[] tmp = new byte[8192];
        while (inputStream.read(tmp) != -1) {}
        String cliCrc = CRC64Utils.longToUnsignedLongString(inputStream.getChecksum().getValue());

        MockWebServer server = new MockWebServer();
        // 第1个请求
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("put succeed"));

        // 第2个请求
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("put succeed").setHeader(TosHeader.HEADER_CRC64, cliCrc));

        // 第3个请求
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("put succeed"));

        try {
            server.start();
            TransportConfig config = TransportConfig.builder().maxRetryCount(3).readTimeoutMills(1000)
                    .writeTimeoutMills(1000).except100ContinueThreshold(0).build();
            Transport transport = new RequestTransport(config);

            // 500 retry
            TosRequest tosRequest = new TosRequest("http", "PUT", server.getHostName(), "/")
                    .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true)
                    .setContent(new ByteArrayInputStream(bodyStr.getBytes())).setContentLength(bodyStr.length());
            TosResponse response = transport.roundTrip(tosRequest);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(StringUtils.toString(response.getInputStream(), "content"), "put succeed");
            Assert.assertEquals(server.getRequestCount(), 3);
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr);

            tosRequest = new TosRequest("http", "PUT", server.getHostName(), "/")
                    .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true)
                    .setContent(new FileInputStream(file)).setContentLength(file.length()).setEnableCrcCheck(true)
                    .setReadLimit(1024*1024*3);
            response = transport.roundTrip(tosRequest);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(StringUtils.toString(response.getInputStream(), "content"), "put succeed");
            Assert.assertEquals(server.getRequestCount(), 6);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr+bodyStr);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr+bodyStr);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr+bodyStr);

            // bounded file
            FileInputStream boundedFis = new FileInputStream(file);
            boundedFis.skip(10);
            tosRequest = new TosRequest("http", "PUT", server.getHostName(), "/")
                    .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true)
                    .setContent(new TosRepeatableBoundedFileInputStream(boundedFis, 100)).setContentLength(100);
            response = transport.roundTrip(tosRequest);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(StringUtils.toString(response.getInputStream(), "content"), "put succeed");
            Assert.assertEquals(server.getRequestCount(), 9);
            String targetData = (bodyStr+bodyStr).substring(10, 110);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), targetData);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), targetData);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), targetData);
        } finally {
            server.shutdown();
            file.delete();
        }
    }

    @Test
    void retryWithBufferedStreamTest() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("put succeed"));
        server.start();

        String dataUrl = "https://www.volcengine.com/docs/6349/79895";
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(dataUrl).openStream())){
            TransportConfig config = TransportConfig.builder().maxRetryCount(3).readTimeoutMills(1000)
                    .writeTimeoutMills(1000).except100ContinueThreshold(0).build();
            Transport transport = new RequestTransport(config);

            TosRequest tosRequest = new TosRequest("http", "PUT", server.getHostName(), "/")
                    .setPort(server.getPort()).setRetryableOnServerException(true).setRetryableOnClientException(true)
                    .setContent(bufferedInputStream).setReadLimit(1024 * 1024 * 1024).setContentLength(-1);
            TosResponse response = transport.roundTrip(tosRequest);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(StringUtils.toString(response.getInputStream(), "content"), "put succeed");
            Assert.assertEquals(server.getRequestCount(), 3);
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            String bodyStr = StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content");
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr);
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(StringUtils.toString(new ByteArrayInputStream(request.getBody().readByteArray()), "content"), bodyStr);
        } finally {
            server.shutdown();
        }
    }

    @Test
    void markTest() {
        try(FileInputStream fileInputStream = new FileInputStream("src/test/resources/uploadPartTest.zip");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE)) {
            bufferedInputStream.mark(Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE);
            byte[] data1 = new byte[Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE];
            bufferedInputStream.read(data1);
            byte[] data2 = new byte[Consts.DEFAULT_TOS_BUFFER_STREAM_SIZE];
            bufferedInputStream.reset();
            bufferedInputStream.read(data2);
            Assert.assertEquals(data1, data2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
