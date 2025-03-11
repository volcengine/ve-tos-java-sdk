package com.volcengine.tos.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.ByteArrayBody;
import org.apache.hc.client5.http.entity.mime.ContentBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.Consts;
import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.internal.model.CreateMultipartUploadOutputJson;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.CompleteMultipartUploadV2Input;
import com.volcengine.tos.model.object.ContentLengthRange;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;
import com.volcengine.tos.model.object.ListObjectsV2Output;
import com.volcengine.tos.model.object.PolicySignatureCondition;
import com.volcengine.tos.model.object.PostSignatureCondition;
import com.volcengine.tos.model.object.PreSignedPolicyURLInput;
import com.volcengine.tos.model.object.PreSignedPolicyURLOutput;
import com.volcengine.tos.model.object.PreSignedPostSignatureInput;
import com.volcengine.tos.model.object.PreSignedPostSignatureOutput;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.UploadedPartV2;

public class TosPreSignedRequestHandlerTest {

    private TosPreSignedRequestHandler handler;
    private CloseableHttpClient client;
    private TOSV2 tosClient;
    private final String sampleData = StringUtils.randomString(1024);

    @BeforeTest
    void init() {
        handler = ClientInstance.getPreSignedRequestHandlerInstance();
        this.client = TosUtils.defaultApacheHttpClient();
        this.tosClient = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());
    }

    @Test
    void preSignedURLCreateDeleteBucketTest() {
        String bucket = Consts.bucket + System.nanoTime();
        // create bucket and delete bucket
        try {
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(bucket).setExpires(1000);
            PreSignedURLOutput url = handler.preSignedURL(input);
            Consts.LOG.debug("url, {}", url.getSignedUrl());
            ClassicHttpResponse resp = doReq(HttpMethod.PUT, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucket).setHttpMethod(HttpMethod.DELETE);
            PreSignedURLOutput url = handler.preSignedURL(input);
            try {
                ClassicHttpResponse resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
                Assert.assertEquals(resp.getCode(), HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Test
    void preSignedURLSpecialCharTest() {
        List<String> keyList = new ArrayList<>();
        keyList.add("/test01/!-_.*'()"); // 无需编码特殊字符
        keyList.add("/test02/&$@=;+    ,?"); // 需要编码特殊字符，包含连续多个空格
        keyList.add("/test03/\t\n\r\b\f\007"); // 包含ASCII码控制字符
        keyList.add("/test04/\uD83D\uDE0A?/\uD83D\uDE2D文本"); // 包含中文、emoji表情
        keyList.add("/test05/[\\{^}%`~<>#|]\""); // 不建议使用的字符
        keyList.add("./test06/./test"); // ./开头以及中间包含./
        keyList.add("../test07/../test"); // ../开头以及中间包含../
        keyList.add("/test08/."); // /.结尾
        keyList.add("/test09/.."); // /..结尾
        keyList.add("/test10///.."); // 包含多个连续的//

        for (String key : keyList) {
            // preSign put object
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                    .setKey(key).setExpires(3600);
            PreSignedURLOutput url = handler.preSignedURL(input);
            Random random = new Random();
            int length = random.nextInt(65536) + 1;
            try {
                InputStream content = new ByteArrayInputStream(StringUtils.randomString(length).getBytes());
                ClassicHttpResponse resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, length, "");
                System.out.println(url.getSignedUrl());
                Assert.assertEquals(resp.getCode(), HttpStatus.OK, resp.toString());
                resp.close();

                // preSign get object
                input = new PreSignedURLInput().setHttpMethod(HttpMethod.GET).setBucket(Consts.bucket).setKey(key);
                url = handler.preSignedURL(input);
                resp = doReq(HttpMethod.GET, url.getSignedUrl(), null, -1, "");
                Assert.assertEquals(resp.getCode(), HttpStatus.OK);
                Assert.assertEquals(resp.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(length));
                Objects.requireNonNull(resp.getEntity()).close();

                // preSign delete object
                input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket).setKey(key);
                url = handler.preSignedURL(input);
                resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
                Assert.assertEquals(resp.getCode(), HttpStatus.NO_CONTENT);

                // put object
                length = random.nextInt(65536) + 1;
                InputStream content1 = new ByteArrayInputStream(StringUtils.randomString(length).getBytes());
                tosClient.putObject(new PutObjectInput().setBucket(Consts.bucket).setKey(key).setContent(content1));

                // get object
                GetObjectV2Output getOutput = tosClient.getObject(new GetObjectV2Input().setBucket(Consts.bucket)
                        .setKey(key));
                Assert.assertEquals(getOutput.getContentLength(), length);
                getOutput.getContent().close();

                // preSign get object
                input = new PreSignedURLInput().setHttpMethod(HttpMethod.GET).setBucket(Consts.bucket).setKey(key);
                url = handler.preSignedURL(input);
                resp = doReq(HttpMethod.GET, url.getSignedUrl(), null, -1, "");
                Assert.assertEquals(resp.getCode(), HttpStatus.OK);
                Assert.assertEquals(resp.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(length));
                Objects.requireNonNull(resp.getEntity()).close();

                // delete object
                input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket).setKey(key);
                url = handler.preSignedURL(input);
                resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
                Assert.assertEquals(resp.getCode(), HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                testFailed(e);
            }
        }
    }

    @Test
    void preSignedURLObjectCURDTest() {
        String key = getUniqueObjectKey();
        try {
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
        try {
            InputStream content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            ClassicHttpResponse resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            resp.close();

            Thread.sleep(3000);
            content = new ByteArrayInputStream(StringUtils.randomString(65536).getBytes());
            resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.FORBIDDEN);
            resp.close();
        } catch (Exception e) {
            testFailed(e);
        }

        // put object with sha256
        Map<String, String> header = new HashMap<>();
        header.put(TosHeader.HEADER_CONTENT_SHA256, "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9");
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(3600).setHeader(header);
        url = handler.preSignedURL(input);
        try {
            InputStream content = new ByteArrayInputStream("hello world".getBytes());
            ClassicHttpResponse resp = doReqWithHeaders(HttpMethod.PUT, url.getSignedUrl(), content, "hello world".length(), "", header);
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
        } catch (IOException e) {
            testFailed(e);
        }

        // head object
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setBucket(Consts.bucket)
                .setKey(key).setExpires(60);
        url = handler.preSignedURL(input);
        String data = StringUtils.randomString(65536);
        try {
            // prepare data
            InputStream content = new ByteArrayInputStream(data.getBytes());
            ClassicHttpResponse resp = doReq(HttpMethod.PUT, url.getSignedUrl(), content, 65536, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            resp.close();

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket)
                    .setKey(key).setExpires(1);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            Thread.sleep(3000);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }

        // get object
        ClassicHttpResponse resp;
        try {
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.GET).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.GET, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            String data1 = StringUtils.toString(resp.getEntity().getContent(), "content");
            resp.getEntity().close();
            Assert.assertTrue(StringUtils.equals(data1, data));
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }

        // delete object
        try {
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket)
                    .setKey(key).setExpires(2);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.NO_CONTENT);
            Thread.sleep(2500);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedURLMultipartUploadTest() {
        String key = getUniqueObjectKey();
        try {
            // createMultipartUpload
            PreSignedURLInput input = new PreSignedURLInput().setHttpMethod(HttpMethod.POST).setBucket(Consts.bucket)
                    .setKey(key).setExpires(120).setQuery(Collections.singletonMap("uploads", ""));
            PreSignedURLOutput url = handler.preSignedURL(input);
            ClassicHttpResponse resp = doReq(HttpMethod.POST, url.getSignedUrl(),
                    null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            CreateMultipartUploadOutputJson create = PayloadConverter.parsePayload(resp.getEntity().getContent(),
                    new TypeReference<CreateMultipartUploadOutputJson>() {
            });
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
                Assert.assertEquals(resp.getCode(), HttpStatus.OK);
                parts.add(new UploadedPartV2().setPartNumber(i).setEtag(resp.getFirstHeader(TosHeader.HEADER_ETAG).getValue()));
            }

            // completeMultipartUpload
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.POST).setBucket(Consts.bucket)
                    .setKey(key).setExpires(120).setQuery(Collections.singletonMap("uploadId", uploadID));
            url = handler.preSignedURL(input);
            TosMarshalResult serializedPayload = PayloadConverter.serializePayloadAndComputeMD5(new CompleteMultipartUploadV2Input().setUploadedParts(parts));
            resp = doReq(HttpMethod.POST, url.getSignedUrl(), new ByteArrayInputStream(serializedPayload.getData()),
                    serializedPayload.getData().length, "application/json");
            resp.close();
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);
            Consts.LOG.debug("completeMultipartUpload succeed, reqid is {}, object key is {}", resp.getFirstHeader(TosHeader.HEADER_REQUEST_ID).getValue(), key);

            // head it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.HEAD).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.HEAD, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.OK);

            // delete it
            input = new PreSignedURLInput().setHttpMethod(HttpMethod.DELETE).setBucket(Consts.bucket).setKey(key);
            url = handler.preSignedURL(input);
            resp = doReq(HttpMethod.DELETE, url.getSignedUrl(), null, -1, "");
            Assert.assertEquals(resp.getCode(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void preSignedURLAlternativeEndpointTest() {
        String key = getUniqueObjectKey();
        try {
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
        System.out.println(url.getSignedUrl());
        Assert.assertTrue(url.getSignedUrl().contains(Consts.bucket + "." + Consts.endpoint2));

        // generate url without bucket
        input = new PreSignedURLInput().setHttpMethod(HttpMethod.PUT).setCustomDomain(true)
                .setKey(key).setExpires(20).setAlternativeEndpoint(Consts.endpoint2);
        url = handler.preSignedURL(input);
        Assert.assertTrue(url.getSignedUrl().startsWith("https://" + Consts.endpoint2 + "/"));
        Consts.LOG.debug("generated url: {}", url.getSignedUrl());
    }

    @Test
    void preSignedPostSignatureTest() {
        String key = getUniqueObjectKey();
        try {
            // base upload
            PreSignedPostSignatureInput input = new PreSignedPostSignatureInput().setBucket(Consts.bucket)
                    .setKey(key).setExpires(3600);
            PreSignedPostSignatureOutput output = handler.preSignedPostSignature(input);
            ClassicHttpResponse response = doPostReq(key, null, output, sampleData);
            if (response.getEntity() != null) {
                Consts.LOG.debug(StringUtils.toString(response.getEntity().getContent(), "content"));
            }
            Assert.assertEquals(response.getCode(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // expires
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(1);
            output = handler.preSignedPostSignature(input);
            Thread.sleep(3000);
            response = doPostReq(key, null, output, sampleData);
            Assert.assertEquals(response.getCode(), HttpStatus.FORBIDDEN);
            response.close();

            // with content length range
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(3600)
                    .setContentLengthRange(new ContentLengthRange().setRangeStart(50).setRangeEnd(1025));
            output = handler.preSignedPostSignature(input);
            response = doPostReq(key, null, output, sampleData);
            if (response.getEntity() != null) {
                Consts.LOG.debug(StringUtils.toString(response.getEntity().getContent(), "content"));
            }
            Assert.assertEquals(response.getCode(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // exceed content range
            key = getUniqueObjectKey();
            input = new PreSignedPostSignatureInput().setBucket(Consts.bucket).setKey(key).setExpires(3600)
                    .setContentLengthRange(new ContentLengthRange().setRangeStart(50).setRangeEnd(1023));
            output = handler.preSignedPostSignature(input);
            response = doPostReq(key, null, output, sampleData);
            if (response.getEntity() != null) {
                Consts.LOG.debug(StringUtils.toString(response.getEntity().getContent(), "content"));
            }
            Assert.assertEquals(response.getCode(), HttpStatus.BAD_REQUEST);
            response.close();
            GetObjectV2Input get = new GetObjectV2Input().setBucket(Consts.bucket).setKey(key);
            try (GetObjectV2Output got = ClientInstance.getObjectRequestHandlerInstance().getObject(get)) {
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
        try {
            String keyPrefix = "post-";
            String key = keyPrefix + getUniqueObjectKey();
            // base upload
            List<PostSignatureCondition> conditions = new ArrayList<>(2);
            conditions.add(new PostSignatureCondition("x-tos-acl", "public-read"));
            conditions.add(new PostSignatureCondition("key", key, "starts-with"));
            PreSignedPostSignatureInput input = new PreSignedPostSignatureInput().setBucket(Consts.bucket)
                    .setKey(key).setExpires(3600).setConditions(conditions);
            PreSignedPostSignatureOutput output = handler.preSignedPostSignature(input);
            ClassicHttpResponse response = doPostReq(key, ACLType.ACL_PUBLIC_READ, output, sampleData);
            if (response.getEntity() != null) {
                Consts.LOG.debug(StringUtils.toString(response.getEntity().getContent(), "content"));
            }
            Assert.assertEquals(response.getCode(), HttpStatus.NO_CONTENT);
            response.close();
            checkData(key);

            // invalid acl
            response = doPostReq(key, ACLType.ACL_PRIVATE, output, sampleData);
            Assert.assertEquals(response.getCode(), HttpStatus.FORBIDDEN);
            response.close();

            // invalid key
            key = getUniqueObjectKey();
            response = doPostReq(key, ACLType.ACL_PUBLIC_READ, output, sampleData);
            Assert.assertEquals(response.getCode(), HttpStatus.FORBIDDEN);
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
        PreSignedPolicyURLInput input = new PreSignedPolicyURLInput().setExpires(1000).setConditions(conditions).setBucket(Consts.bucket);
        PreSignedPolicyURLOutput output = handler.preSignedPolicyURL(input);

        String getUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key, null);
        System.out.println(getUrl);
        try {
            ClassicHttpResponse response = doReq(HttpMethod.GET, getUrl, null, -1, "");
            String body = null;
            if (response.getEntity() != null) {
                body = StringUtils.toString(response.getEntity().getContent(), "content");
                response.close();
            }
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertEquals(body, data);

            String headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key1, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(data1.length()));

            // prefix must be subsequence for policy, prefix set "policy", but policy starts-with "policy-"
            Map<String, String> query = new HashMap<>();
            query.put("prefix", "policy");
            String listUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForList(query);
            response = doReq(HttpMethod.GET, listUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.FORBIDDEN);
            if (response.getEntity() != null) {
                response.close();
            }

            query = new HashMap<>();
            query.put("prefix", keyPrefix);
            listUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForList(query);
            response = doReq(HttpMethod.GET, listUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertNotNull(response.getEntity());
            ListObjectsV2Output listed = PayloadConverter.parsePayload(response.getEntity().getContent(),
                    new TypeReference<ListObjectsV2Output>() {
            });
            response.close();
            Assert.assertNotNull(listed);
            Assert.assertNotNull(listed.getContents());
            // key and key1
            Assert.assertTrue(listed.getContents().size() >= 2);

            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key2, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(data2.length()));

            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key3, null);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(data3.length()));
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_TYPE).getValue(), "binary/octet-stream");

            query = new HashMap<>();
            query.put("response-content-type", "text/plain");
            headUrl = output.getPreSignedPolicyURLGenerator().getSignedURLForGetOrHead(key3, query);
            response = doReq(HttpMethod.HEAD, headUrl, null, -1, "");
            Assert.assertEquals(response.getCode(), HttpStatus.OK);
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_LENGTH).getValue(), String.valueOf(data3.length()));
            Assert.assertEquals(response.getFirstHeader(TosHeader.HEADER_CONTENT_TYPE).getValue(), "text/plain");
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
        try (GetObjectV2Output got = ClientInstance.getObjectRequestHandlerInstance().getObject(get)) {
            String gotData = StringUtils.toString(got.getContent(), "content");
            Assert.assertEquals(TosObjectRequestHandlerBasicTest.getContentMD5(gotData),
                    TosObjectRequestHandlerBasicTest.getContentMD5(sampleData));
        }
    }

    private ClassicHttpResponse doReq(String method, String url, InputStream content, long contentLength, String contentType) throws IOException {
        return this.doReqWithHeaders(method, url, content, contentLength, contentType, null);
    }

    private ClassicHttpResponse doReqWithHeaders(String method, String url, InputStream content, long contentLength, String contentType, Map<String, String> headers) throws IOException {
        ClassicHttpRequest request = null;
        if (StringUtils.isEmpty(contentType)) {
            contentType = "binary/octet-stream";
        }
        switch (method.toUpperCase()) {
            case HttpMethod.GET:
                request = new HttpGet(url);
                break;
            case HttpMethod.POST:
                request = new HttpPost(url);
                byte[] data;
                if (content == null) {
                    data = new byte[0];
                } else {
                    data = StringUtils.toByteArray(content);
                }
                request.setEntity(new ByteArrayEntity(data, org.apache.hc.core5.http.ContentType.parse(contentType)));
                break;
            case HttpMethod.PUT: {
                request = new HttpPut(url);
                if (content != null) {
                    request.setEntity(new WrappedApacheTransportRequestBody(org.apache.hc.core5.http.ContentType.parse(contentType), content, contentLength));
                } else {
                    request.setEntity(new ByteArrayEntity(new byte[0], org.apache.hc.core5.http.ContentType.parse(contentType)));
                }
                break;
            }
            case HttpMethod.HEAD:
                request = new HttpHead(url);
                break;
            case HttpMethod.DELETE:
                request = new HttpDelete(url);
                break;
            default:
                throw new UnsupportedOperationException("Method is not supported: " + method);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return client.executeOpen(null, request, null);
    }

    private ClassicHttpResponse doPostReq(String key, ACLType acl, PreSignedPostSignatureOutput input, String data) throws IOException {
        String url = "http://" + Consts.bucket + "." + Consts.endpoint;
        //RequestBody contentBody = RequestBody.create(MediaType.parse("binary/octet-stream"), data);
        ClassicHttpRequest request = new HttpPost(url);
        System.out.println("key: " + key);
        System.out.println("bucket: " + Consts.bucket);
        System.out.println("endpoint: " + Consts.endpoint);
        System.out.println("X-Tos-Algorithm: " + input.getAlgorithm());
        System.out.println("X-Tos-Date: " + input.getDate());
        System.out.println("X-Tos-Credential: " + input.getCredential());
        System.out.println("X-Tos-Signature: " + input.getSignature());
        System.out.println("policy: " + input.getPolicy());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder = builder.addTextBody("key", key)
                .addTextBody("X-Tos-Algorithm", input.getAlgorithm())
                .addTextBody("X-Tos-Date", input.getDate())
                .addTextBody("X-Tos-Credential", input.getCredential())
                .addTextBody("policy", input.getPolicy())
                .addTextBody("X-Tos-Signature", input.getSignature());;

        if (acl != null) {
            builder = builder.addTextBody("x-tos-acl", acl.toString());
        }
        ContentBody contentBody = new ByteArrayBody(data.getBytes(), org.apache.hc.core5.http.ContentType.parse("binary/octet-stream"), "my.test");
        builder.addPart("file", contentBody);
        request.setEntity(builder.build());

        return client.executeOpen(null, request, null);
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
