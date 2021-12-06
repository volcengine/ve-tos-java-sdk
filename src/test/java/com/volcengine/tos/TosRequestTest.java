package com.volcengine.tos;

import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.PutObjectOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TosRequestTest {
    private static final Logger LOG = LoggerFactory.getLogger(TosRequestTest.class);
    @Test
    public void RequestURLTest(){
//        /abc/😊?/😭#~!.txt
        TosRequest req = new TosRequest("https", HttpMethod.GET, "localhost",
                "/abc/\uD83D\uDE0A?/\uD83D\uDE2D#~!.txt");
        Map<String, String> query = new HashMap<>();
        query.put("versionId", "abc123");
        req.setQuery(query);
        String u = req.toURL().toString();
//        "https://localhost/abc/%F0%9F%98%8A%3F/%F0%9F%98%AD%23~%21.txt?versionId=abc123"
        Assert.assertEquals("https://localhost/abc/%F0%9F%98%8A%3F/%F0%9F%98%AD%23~!.txt?versionId=abc123", u);
    }

    @Test
    public void NotMarshalInfoTest() {
        Map<String, String> header = new HashMap<>();
        header.put(TosHeader.HEADER_CONTENT_TYPE, "application/json");
        PutObjectOutput output = new PutObjectOutput().setRequestInfo(new RequestInfo("bbb", header))
                .setEtag("ccc");
        String data = "{ \"RequestInfo\": { \"RequestID\": \"aaa\" }, \"RequestID\": \"ddd\", \"ETag\": \"abs\", \"VersionId\": \"vid\" }";
        try{
            byte[] out = Consts.JSON.writeValueAsBytes(output);
        } catch (JsonProcessingException e){
            LOG.error(e.getMessage());
        }
        try{
            output = Consts.JSON.readValue(data, new TypeReference<PutObjectOutput>() {});
        } catch (IOException e){
            LOG.error(e.getMessage());
        }
        Assert.assertEquals("abs", output.getEtag());
        // Jackson会覆盖原变量的值，这与go json.unmarshal不一样
//        assertEquals("bbb", output.getRequestInfo().getRequestID());
//        assertEquals("application/json", output.getRequestInfo().getHeader().get(HttpHeaders.HeaderContentType));
    }

    @Test
    public void TryResolveLengthTest() {
        try{
            FileInputStream f = new FileInputStream("pom.xml");
            int size = RequestBuilder.tryResolveLength(f);
            Assert.assertTrue(size>0);
            byte[] data = new byte[1024];
            BufferedInputStream buffers = new BufferedInputStream(new ByteArrayInputStream(data));
            size = RequestBuilder.tryResolveLength(buffers);
            Assert.assertEquals(1024, size);
        } catch (IOException e){
            LOG.error(e.getMessage());
            Assert.fail();
        }
    }
}
