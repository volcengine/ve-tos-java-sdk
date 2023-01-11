package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.Consts;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.model.CreateMultipartUploadOutputJson;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.CompleteMultipartUploadV2Input;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import com.volcengine.tos.model.object.UploadedPartV2;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TosPreSignedRequestHandlerTest {
    private TosPreSignedRequestHandler handler;
    private OkHttpClient client;
    @BeforeTest
    void init() {
        handler = ClientInstance.getPreSignedRequestHandlerInstance();
        ConnectionPool connectionPool = new ConnectionPool(1024, 60000, TimeUnit.MILLISECONDS);

        Dispatcher dispatcher = new Dispatcher();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        this.client = builder.dispatcher(dispatcher)
                .connectionPool(connectionPool).retryOnConnectionFailure(true).connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS).writeTimeout(30000, TimeUnit.MILLISECONDS)
                .followRedirects(false).followSslRedirects(false).build();
    }

    @Test
    void preSignedURLCreateDeleteBucketTest() {
        String bucket = Consts.bucket + System.nanoTime();
        // create bucket and delete bucket
        try{
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(bucket).setExpires(1000);
            PreSignedURLOutput url = handler.preSignedURL(input);
            Consts.LOG.debug("url, {}", url.getSignedUrl());
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucket).setHttpMethod(HttpMethod.DELETE);
            PreSignedURLOutput url = handler.preSignedURL(input);
            try{
                Response resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
                Assert.assertEquals(resp.code(), HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Test
    void preSignedURLObjectCURDTest() {
        String key = getUniqueObjectKey();
        try{
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                    .setKey(key).setExpires(604800 * 10);
            handler.preSignedURL(input);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof TosClientException);
        }

        // put object
        PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(2);
        PreSignedURLOutput url = handler.preSignedURL(input);
        try{
            InputStream content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), url.getSignedHeader(), content, 65536, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            resp.close();

            Thread.sleep(2500);
            content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            resp = doReq(HttpMethod.PUT, url.getSignedUrl(), url.getSignedHeader(), content, 65536, "");
            Assert.assertEquals(resp.code(), HttpStatus.FORBIDDEN);
            resp.close();
        } catch (Exception e) {
            testFailed(e);
        }

        // head object
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(60);
        url = handler.preSignedURL(input);
        String data = StringUtils.randomString(65536);
        try{
            // prepare data
            InputStream content = new ByteArrayInputStream(data.getBytes());
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), url.getSignedHeader(), content, 65536, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            resp.close();

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }

        // get object
        Response resp;
        try{
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.GET).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.GET, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            String data1 = StringUtils.toString(resp.body().byteStream());
            resp.body().close();
            Assert.assertTrue(StringUtils.equals(data1, data));
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }

        // delete object
        try{
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.NO_CONTENT);
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedURLMultipartUploadTest() {
        String key = getUniqueObjectKey();
        try{
            // createMultipartUpload
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.POST).setBucket(Consts.bucket)
                    .setKey(key).setExpires(120).setQuery(Collections.singletonMap("uploads", ""));
            PreSignedURLOutput url = handler.preSignedURL(input);
            Response resp = doReq(HttpMethod.POST, url.getSignedUrl(), url.getSignedHeader(),
                    null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            CreateMultipartUploadOutputJson create = PayloadConverter.parsePayload(resp.body().byteStream(),
                    new TypeReference<CreateMultipartUploadOutputJson>(){});
            String uploadID = create.getUploadID();

            // uploadPart
            List<UploadedPartV2> parts = new ArrayList<>(3);
            byte[] data = new byte[5 * 1024 * 1024];
            Arrays.fill(data, (byte) 'A');
            for (int i = 1; i <= 3; i++) {
                Map<String, String> query = new HashMap<>(2);
                query.put("uploadId", uploadID);
                query.put("partNumber", String.valueOf(i));
                input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                        .setKey(key).setQuery(query).setExpires(120);
                url = handler.preSignedURL(input);
                resp = doReq(HttpMethod.PUT, url.getSignedUrl(), url.getSignedHeader(),
                        new ByteArrayInputStream(data), data.length, "");
                Assert.assertEquals(resp.code(), HttpStatus.OK);
                parts.add(new UploadedPartV2().setPartNumber(i).setEtag(resp.header(TosHeader.HEADER_ETAG)));
            }

            // completeMultipartUpload
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.POST).setBucket(Consts.bucket)
                    .setKey(key).setExpires(120).setQuery(Collections.singletonMap("uploadId", uploadID));
            url = handler.preSignedURL(input);
            TosMarshalResult serializedPayload = PayloadConverter.serializePayload(new CompleteMultipartUploadV2Input().setUploadedParts(parts));
            resp = doReq(HttpMethod.POST, url.getSignedUrl(), url.getSignedHeader(), new ByteArrayInputStream(serializedPayload.getData()),
                    serializedPayload.getData().length, "application/json");
            resp.close();
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Consts.LOG.debug("completeMultipartUpload succeed, reqid is {}, object key is {}", resp.header(TosHeader.HEADER_REQUEST_ID), key);

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);

            // delete it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), url.getSignedHeader(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedURLAlternativeEndpointTest() {
        String key = getUniqueObjectKey();
        try{
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                    .setKey(key).setExpires(604800 * 10).setAlternativeEndpoint(Consts.alternativeEndpoint);
            handler.preSignedURL(input);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof TosClientException);
        }

        // put object
        PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.alternativeEndpoint);
        PreSignedURLOutput url = handler.preSignedURL(input);
        Assert.assertTrue(url.getSignedUrl().contains(Consts.bucket + "." + Consts.alternativeEndpoint));
    }

    private Response doReq(String method, String url, Map<String, String> headers, InputStream content, long contentLength, String contentType) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::header);
        }
        if (StringUtils.isEmpty(contentType)) {
            contentType = "binary/octet-stream";
        }
        switch (method.toUpperCase()) {
            case HttpMethod.GET:
                builder.get();
                break;
            case HttpMethod.POST:
                byte[] data;
                if (content == null) {
                    data = new byte[0];
                } else {
                    data = StringUtils.toByteArray(content);
                }
                builder.post(RequestBody.create(MediaType.parse(contentType), data));
                break;
            case HttpMethod.PUT: {
                if (content != null) {
                    builder.put(new WrappedTransportRequestBody(MediaType.parse(contentType), content, contentLength));
                } else {
                    builder.put(RequestBody.create(MediaType.parse(contentType), ""));
                }
                break;
            }
            case HttpMethod.HEAD:
                builder.head();
                break;
            case HttpMethod.DELETE:
                builder.delete();
                break;
            default:
                throw new UnsupportedOperationException("Method is not supported: " + method);
        }
        return client.newCall(builder.build()).execute();
    }

    private String getUniqueObjectKey() {
        return StringUtils.randomString(10);
    }

    private void testFailed(Exception e) {
        Consts.LOG.error("preSigned test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
