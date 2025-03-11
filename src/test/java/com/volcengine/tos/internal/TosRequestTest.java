package com.volcengine.tos.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.Consts;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.PutObjectOutput;

public class TosRequestTest {

    private static final Logger LOG = LoggerFactory.getLogger(TosRequestTest.class);

    @Test
    public void requestURLTest() throws URISyntaxException {
//        /abc/😊?/😭#~!.txt
        TosRequest req = new TosRequest("https", HttpMethod.GET, "localhost",
                "/abc/\uD83D\uDE0A?/\uD83D\uDE2D#~!.txt");
        Map<String, String> query = new HashMap<>();
        query.put("versionId", "abc123");
        req.setQuery(query);
        String u = req.toURL().toString();
//        "https://localhost/abc/%F0%9F%98%8A%3F/%F0%9F%98%AD%23~%21.txt?versionId=abc123"
        Assert.assertEquals("https://localhost/abc%2F%F0%9F%98%8A%3F%2F%F0%9F%98%AD%23~!.txt?versionId=abc123", u);
        String escapeUrl = req.toEscapeURL().toString();
        Assert.assertEquals("https://localhost/abc/%F0%9F%98%8A%3F/%F0%9F%98%AD%23~!.txt?versionId=abc123", escapeUrl);
    }

    @Test
    public void notMarshalInfoTest() {
        Map<String, String> header = new HashMap<>();
        header.put(TosHeader.HEADER_CONTENT_TYPE, "application/json");
        PutObjectOutput output = new PutObjectOutput().setRequestInfo(new RequestInfo("bbb", header))
                .setEtag("ccc");
        String data = "{ \"RequestInfo\": { \"RequestID\": \"aaa\" }, \"RequestID\": \"ddd\", \"ETag\": \"abs\", \"VersionId\": \"vid\" }";
        try {
            byte[] out = com.volcengine.tos.Consts.JSON.writeValueAsBytes(output);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        try {
            output = Consts.JSON.readValue(data, new TypeReference<PutObjectOutput>() {
            });
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        Assert.assertEquals("abs", output.getEtag());
//        assertEquals("bbb", output.getRequestInfo().getRequestID());
//        assertEquals("application/json", output.getRequestInfo().getHeader().get(HttpHeaders.HeaderContentType));
    }

    @Test
    void pathModeTest() throws URISyntaxException {
        Credentials credentials = new StaticCredentials(Consts.accessKey, Consts.secretKey);
        Signer signer = new SignV4(credentials, Consts.region);
        String endpoint = "http://localhost:8080";
        TosRequestFactory factory = new TosRequestFactory(signer, endpoint);
        TosRequest request = factory.init("aaa", "bbb", null).buildRequest(HttpMethod.PUT, null);
        URI url = request.toURL();
        Assert.assertNotNull(url);
        Assert.assertEquals(url.getHost(), "localhost");
        Assert.assertEquals(url.getPort(), 8080);
        Assert.assertEquals(url.getPath(), "/aaa%2Fbbb");

        endpoint = "http://192.161.1.1:1234";
        factory = new TosRequestFactory(signer, endpoint);
        request = factory.init("aaa", "bbb", null).buildRequest(HttpMethod.PUT, null);
        url = request.toURL();
        Assert.assertNotNull(url);
        Assert.assertEquals(url.getHost(), "192.161.1.1");
        Assert.assertEquals(url.getPort(), 1234);
        Assert.assertEquals(url.getPath(), "/aaa%2Fbbb");

        endpoint = "http://[fe80::1551:1234:fbb:fcc3]:1234";
        factory = new TosRequestFactory(signer, endpoint);
        request = factory.init("aaa", "bbb", null).buildRequest(HttpMethod.PUT, null);
        url = request.toURL();
        Assert.assertNotNull(url);
        Assert.assertEquals(url.getHost(), "fe80::1551:1234:fbb:fcc3");
        Assert.assertEquals(url.getPort(), 1234);
        Assert.assertEquals(url.getPath(), "/aaa%2Fbbb");

        // 自定义域名
        endpoint = "custom.domain.test";
        factory = new TosRequestFactory(signer, endpoint);
        request = factory.setIsCustomDomain(true).init("aaa", "bbb", null)
                .buildRequest(HttpMethod.PUT, null);
        url = request.toURL();
        Assert.assertNotNull(url);
        Assert.assertEquals(url.getHost(), "custom.domain.test");
        Assert.assertEquals(url.getPort(), 443);
        Assert.assertEquals(url.getPath(), "/bbb");
        Consts.LOG.info(url.toString());
        Assert.assertEquals(url.toString(), "https://custom.domain.test/bbb");
    }

    @Test
    void proxyTest() {
        // to do, there is no proxy can be gotten for closeable http
    }
}
