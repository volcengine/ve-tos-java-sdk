package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.Consts;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.internal.model.CreateMultipartUploadOutputJson;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.*;
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
    private final String sampleData = StringUtils.randomString(1024);
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
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucket).setHttpMethod(HttpMethod.DELETE);
            PreSignedURLOutput url = handler.preSignedURL(input);
            try{
                Response resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
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
        } catch (Exception e) {
            Assert.fail();
        }

        // put object
        PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(1);
        PreSignedURLOutput url = handler.preSignedURL(input);
        try{
            InputStream content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            resp.close();

            Thread.sleep(3000);
            content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
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
            Response resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            resp.close();

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket)
                    .setKey(key).setExpires(1);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Thread.sleep(3000);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
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
            resp = doReq(HttpMethod.GET, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            String data1 = StringUtils.toString(resp.body().byteStream(), "content");
            resp.body().close();
            Assert.assertTrue(StringUtils.equals(data1, data));
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }

        // delete object
        try{
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.NO_CONTENT);
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
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
            Response resp = doReq(HttpMethod.POST, url.getSignedUrl(),
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
                resp = doReq(HttpMethod.PUT, url.getSignedUrl(),
                        new ByteArrayInputStream(data), data.length, "");
                Assert.assertEquals(resp.code(), HttpStatus.OK);
                parts.add(new UploadedPartV2().setPartNumber(i).setEtag(resp.header(TosHeader.HEADER_ETAG)));
            }

            // completeMultipartUpload
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.POST).setBucket(Consts.bucket)
                    .setKey(key).setExpires(120).setQuery(Collections.singletonMap("uploadId", uploadID));
            url = handler.preSignedURL(input);
            TosMarshalResult serializedPayload = PayloadConverter.serializePayloadAndComputeMD5(new CompleteMultipartUploadV2Input().setUploadedParts(parts));
            resp = doReq(HttpMethod.POST, url.getSignedUrl(), new ByteArrayInputStream(serializedPayload.getData()),
                    serializedPayload.getData().length, "application/json");
            resp.close();
            Assert.assertEquals(resp.code(), HttpStatus.OK);
            Consts.LOG.debug("completeMultipartUpload succeed, reqid is {}, object key is {}", resp.header(TosHeader.HEADER_REQUEST_ID), key);

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.code(), HttpStatus.OK);

            // delete it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
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
                    .setKey(key).setExpires(604800 * 10).setAlternativeEndpoint(Consts.endpoint2);
            handler.preSignedURL(input);
        } catch (Exception e) {
            Assert.fail();
        }

        // put object
        PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(2).setAlternativeEndpoint(Consts.endpoint2);
        PreSignedURLOutput url = handler.preSignedURL(input);
        Assert.assertTrue(url.getSignedUrl().contains(Consts.bucket + "." + Consts.endpoint2));

        // generate url without bucket
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT)
                .setKey(key).setExpires(20).setAlternativeEndpoint(Consts.endpoint2);
        url = handler.preSignedURL(input);
        Assert.assertTrue(url.getSignedUrl().startsWith("https://" + Consts.endpoint2 + "/"));
        Consts.LOG.debug("generated url: {}", url.getSignedUrl());
    }

    @Test
    void preSignedPostSignatureTest() {
        String key = getUniqueObjectKey();
        try{
            // base upload
            PreSignedPostSignatureInput input = new PreSignedPostSignatureInput().setBucket(Consts.bucket)
                    .setKey(key).setExpires(3600);
            PreSignedPostSignatureOutput output = handler.preSignedPostSignature(input);
            Response response = doPostReq(key, null, output, sampleData);
            if (response.body() != null) {
                Consts.LOG.debug(StringUtils.toString(response.body().byteStream(), "content"));
            }
            Assert.assertEquals(response.code(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // expires
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(1);
            output = handler.preSignedPostSignature(input);
            Thread.sleep(3000);
            response = doPostReq(key, null, output, sampleData);
            Assert.assertEquals(response.code(), HttpStatus.FORBIDDEN);
            response.close();

            // with content length range
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(3600)
                    .setContentLengthRange(new ContentLengthRange().setRangeStart(50).setRangeEnd(1025));
            output = handler.preSignedPostSignature(input);
            response = doPostReq(key, null, output, sampleData);
            if (response.body() != null) {
                Consts.LOG.debug(StringUtils.toString(response.body().byteStream(), "content"));
            }
            Assert.assertEquals(response.code(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // exceed content range
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(3600)
                    .setContentLengthRange(new ContentLengthRange().setRangeStart(50).setRangeEnd(1023));
            output = handler.preSignedPostSignature(input);
            response = doPostReq(key, null, output, sampleData);
            if (response.body() != null) {
                Consts.LOG.debug(StringUtils.toString(response.body().byteStream(), "content"));
            }
            Assert.assertEquals(response.code(), HttpStatus.BAD_REQUEST);
            response.close();
            GetObjectV2Input get = new GetObjectV2Input().setBucket(Consts.bucket).setKey(key);
            try(GetObjectV2Output got = ClientInstance.getObjectRequestHandlerInstance().getObject(get)) {
                String gotData = StringUtils.toString(got.getContent(), "content");
                Consts.LOG.debug(gotData);
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedPostSignatureWithConditionTest() {
        try{
            String keyPrefix = "post-";
            String key = keyPrefix + getUniqueObjectKey();
            // base upload
            List<PostSignatureCondition> conditions = new ArrayList<>(2);
            conditions.add(new PostSignatureCondition("x-tos-acl", "public-read"));
            conditions.add(new PostSignatureCondition("key", key, "starts-with"));
            PreSignedPostSignatureInput input = new PreSignedPostSignatureInput().setBucket(Consts.bucket)
                    .setKey(key).setExpires(3600).setConditions(conditions);
            PreSignedPostSignatureOutput output = handler.preSignedPostSignature(input);
            Response response = doPostReq(key, ACLType.ACL_PUBLIC_READ, output, sampleData);
            if (response.body() != null) {
                Consts.LOG.debug(StringUtils.toString(response.body().byteStream(), "content"));
            }
            Assert.assertEquals(response.code(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // invalid acl
            response = doPostReq(key, ACLType.ACL_PRIVATE, output, sampleData);
            Assert.assertEquals(response.code(), HttpStatus.FORBIDDEN);
            response.close();

            // invalid key
            key = getUniqueObjectKey();
            response = doPostReq(key, ACLType.ACL_PUBLIC_READ, output, sampleData);
            Assert.assertEquals(response.code(), HttpStatus.FORBIDDEN);
            response.close();
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedPolicyURLTest() {
        String uniqKey = "policy" + System.nanoTime();
        String keyPrefix = uniqKey + "-";
        String key = keyPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        String key1 = keyPrefix + getUniqueObjectKey();
        String data1 = sampleData + StringUtils.randomString(new Random().nextInt(128));
        String key2 = uniqKey + "/" + getUniqueObjectKey();
        String data2 = sampleData + StringUtils.randomString(new Random().nextInt(128));
        String key3 = uniqKey + "/" + getUniqueObjectKey();
        String data3 = sampleData + StringUtils.randomString(new Random().nextInt(128));

        genData(key, data, key1, data1, key2, data2, key3, data3);

        // build policy url
        String operatorStartWith = "starts-with";
        String operatorEq = "eq";

        List<PolicySignatureCondition> conditions = new ArrayList<>();
        PolicySignatureCondition condition = new PolicySignatureCondition().setKey("key").setValue(keyPrefix).setOperator(operatorStartWith);
        conditions.add(condition);
        condition = new PolicySignatureCondition().setKey("key").setValue(key2).setOperator(operatorEq);
        conditions.add(condition);
        condition = new PolicySignatureCondition().setKey("key").setValue(key3);
        conditions.add(condition);
        PreSingedPolicyURLInput input = new PreSingedPolicyURLInput().setExpires(1000).setConditions(conditions).setBucket(Consts.bucket);
        PreSingedPolicyURLOutput output = handler.preSingedPolicyURL(input);

        String getUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key, null);
        try{
            Response response = doReq(HttpMethod.GET, getUrl, null, -1, "");
            String body = null;
            if (response.body() != null) {
                body = StringUtils.toString(response.body().byteStream(), "content");
//                Consts.LOG.debug(body);
                response.close();
            }
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertEquals(body, data);

            String headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key1, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_LENGTH), String.valueOf(data1.length()));

            // prefix must be subsequence for policy, prefix set "policy", but policy starts-with "policy-"
            Map<String, String> query = new HashMap<>();
            query.put("prefix", "policy");
            String listUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForList(query);
            response = doReq(HttpMethod.GET, listUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.FORBIDDEN);
            if (response.body() != null) {
                response.close();
            }

            query = new HashMap<>();
            query.put("prefix", keyPrefix);
            listUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForList(query);
            response = doReq(HttpMethod.GET, listUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertNotNull(response.body());
            ListObjectsV2Output listed = PayloadConverter.parsePayload(response.body().byteStream(),
                    new TypeReference<ListObjectsV2Output>(){});
            response.close();
            Assert.assertNotNull(listed);
            Assert.assertNotNull(listed.getContents());
            // key and key1
            Assert.assertTrue(listed.getContents().size() >= 2);

            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key2, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_LENGTH), String.valueOf(data2.length()));

            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key3, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_LENGTH), String.valueOf(data3.length()));
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_TYPE), "binary/octet-stream");

            query = new HashMap<>();
            query.put("response-content-type", "text/plain");
            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key3, query);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.code(), HttpStatus.OK);
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_LENGTH), String.valueOf(data3.length()));
            Assert.assertEquals(response.header(TosHeader.HEADER_CONTENT_TYPE), "text/plain");
        } catch (IOException e) {
            testFailed(e);
        }
    }

    private void genData(String key, String data, String key1, String data1, String key2, String data2, String key3, String data3) {
        PutObjectInput input = new PutObjectInput().setBucket(Consts.bucket).setKey(key)
                .setContent(new ByteArrayInputStream(data.getBytes()));
        ClientInstance.getObjectRequestHandlerInstance().putObject(input);
        input = new PutObjectInput().setBucket(Consts.bucket).setKey(key1)
                .setContent(new ByteArrayInputStream(data1.getBytes()));
        ClientInstance.getObjectRequestHandlerInstance().putObject(input);
        input = new PutObjectInput().setBucket(Consts.bucket).setKey(key2)
                .setContent(new ByteArrayInputStream(data2.getBytes()));
        ClientInstance.getObjectRequestHandlerInstance().putObject(input);
        input = new PutObjectInput().setBucket(Consts.bucket).setKey(key3)
                .setContent(new ByteArrayInputStream(data3.getBytes()));
        ClientInstance.getObjectRequestHandlerInstance().putObject(input);
    }

    private void checkData(String key) throws IOException {
        GetObjectV2Input get = new GetObjectV2Input().setBucket(Consts.bucket).setKey(key);
        try(GetObjectV2Output got = ClientInstance.getObjectRequestHandlerInstance().getObject(get)) {
            String gotData = StringUtils.toString(got.getContent(), "content");
            Assert.assertEquals(TosObjectRequestHandlerBasicTest.getContentMD5(gotData),
                    TosObjectRequestHandlerBasicTest.getContentMD5(sampleData));
        }
    }

    private Response doReq(String method, String url, InputStream content, long contentLength, String contentType) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
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
                    builder.put(new WrappedTransportRequestBody(MediaType.parse(contentType),
                            new TosRequest().setContent(content).setContentLength(contentLength)));
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

    private Response doPostReq(String key, ACLType acl, PreSignedPostSignatureOutput input, String data) throws IOException {
        String url = "http://" + Consts.bucket + "." + Consts.endpoint;
        RequestBody contentBody = RequestBody.create(MediaType.parse("binary/octet-stream"), data);
        System.out.println("key: " + key);
        System.out.println("bucket: " + Consts.bucket);
        System.out.println("endpoint: " + Consts.endpoint);
        System.out.println("X-Tos-Algorithm: " + input.getAlgorithm());
        System.out.println("X-Tos-Date: " + input.getDate());
        System.out.println("X-Tos-Credential: " + input.getCredential());
        System.out.println("X-Tos-Signature: " + input.getSignature());
        System.out.println("policy: " + input.getPolicy());

        MultipartBody.Builder reqBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .addFormDataPart("X-Tos-Algorithm", input.getAlgorithm())
                .addFormDataPart("X-Tos-Date", input.getDate())
                .addFormDataPart("X-Tos-Credential", input.getCredential())
                .addFormDataPart("policy", input.getPolicy())
                .addFormDataPart("X-Tos-Signature", input.getSignature());
        if (acl != null) {
            reqBuilder.addFormDataPart("x-tos-acl", acl.toString());
        }
        reqBuilder.addFormDataPart("file", "my.test", contentBody);
        MultipartBody body = reqBuilder.build();

        Request request = new Request.Builder().url(url).addHeader("content-type", "multipart/form-data").post(body).build();
        return client.newCall(request).execute();
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
