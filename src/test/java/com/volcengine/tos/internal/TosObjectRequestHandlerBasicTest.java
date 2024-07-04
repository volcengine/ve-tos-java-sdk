package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.*;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.base64.Base64;
import com.volcengine.tos.internal.util.ratelimit.DefaultRateLimiter;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.GranteeV2;
import com.volcengine.tos.model.acl.Owner;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.bucket.Tag;
import com.volcengine.tos.model.object.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.zip.CheckedInputStream;

import static com.volcengine.tos.Consts.*;

public class TosObjectRequestHandlerBasicTest {
    private static final String sampleData = StringUtils.randomString(128 << 10);
    private static final String sampleFilePath = "src/test/resources/uploadPartTest.zip";
    private static String sampleFileMD5 = null;
    private static final String ssecKey = "Y2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2M=";
    private static final String ssecKeyMD5 = "ACdH+Fu9K3HlXdIUBu8GdA==";
    private static final String ssecAlgorithm = "AES256";

    private TosObjectRequestHandler getHandler() {
        return ClientInstance.getObjectRequestHandlerInstance();
    }

    @BeforeTest
    void init() throws InterruptedException {
        try {
            HeadBucketV2Output head = ClientInstance.getBucketRequestHandlerInstance()
                    .headBucket(HeadBucketV2Input.builder().bucket(Consts.bucket).build());
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try {
                    ClientInstance.getBucketRequestHandlerInstance().createBucket(
                            CreateBucketV2Input.builder().bucket(Consts.bucket).build()
                    );
                    Thread.sleep(10000);
                } catch (TosException te) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        // ignore
                    }
                    testFailed(e);
                }
            } else {
                testFailed(e);
            }
        }
        try {
            HeadBucketV2Output head = ClientInstance.getBucketRequestHandlerInstance()
                    .headBucket(HeadBucketV2Input.builder().bucket(Consts.bucketCopy).build());
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try {
                    ClientInstance.getBucketRequestHandlerInstance().createBucket(
                            CreateBucketV2Input.builder().bucket(Consts.bucketCopy).build()
                    );
                    Thread.sleep(10000);
                } catch (TosException te) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        // ignore
                    }
                    testFailed(e);
                }
            } else {
                testFailed(e);
            }
        }
    }

    @Test
    void invalidObjectKeyTest() {
        // invisible char
        // can not be \x00
        char[] invisibleChars = new char[32];
        for (int i = 0; i < 32; ++i) {
            invisibleChars[i] = (char) (i + 1);
        }
        String invisibleString = Arrays.toString(invisibleChars);
        try {
            getHandler().putObject(PutObjectInput.builder().bucket(Consts.bucketCopy).key(invisibleString).build());
        } catch (TosClientException e) {
            Assert.assertEquals(e.getMessage(), "object key should not contain invisible unicode characters");
        }

        invisibleChars = new char[128];
        for (int i = 0; i < 128; ++i) {
            invisibleChars[i] = (char) (i + 128 + 1);
        }
        invisibleString = Arrays.toString(invisibleChars);
        try {
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(invisibleString).build();
            getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
        } catch (TosClientException e) {
            Assert.assertEquals(e.getMessage(), "object key should not contain invisible unicode characters");
        }


        // invalid length
        List<String> invalidKeyList = Arrays.asList(null, "");
        for (String key : invalidKeyList) {
            try {
                getHandler().putObject(PutObjectInput.builder().bucket(Consts.bucket).key(key).build());
                Assert.fail();
            } catch (TosClientException e) {
                Assert.assertEquals(e.getMessage(), "invalid object name, the length must be larger than 0");
            }
        }

//        // invalid / \\ "\\aaa",
//        invalidKeyList = Arrays.asList("/aa");
//        for (String key : invalidKeyList) {
//            try {
//                PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(key).build();
//                getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
//            } catch (TosClientException e) {
//            Assert.assertEquals(e.getMessage(), "invalid object name, the object name can not start with / or \\");
//            }
//        }

        invalidKeyList = Arrays.asList(".", "..", "%2e", "%2e%2e", "%2E", "%2E%2E", ".%2e", "%2e.", ".%2E", "%2E.");
        for (String key : invalidKeyList) {
            try {
                getHandler().putObject(PutObjectInput.builder().bucket(Consts.bucket).key(key).build());
            } catch (TosClientException e) {
                Assert.assertEquals(e.getMessage(), "object key should not be . or ..");
            }
        }
    }

    @Test
    void validObjectKeySpecialTest() {
        // special valid case

        // Actually these object keys are valid online, but they are not valid in the test environment.
//        List<String> validDotWithSegmentList = Arrays.asList("./", "a/.", "../", "a/..", "a//a", "a///a");

        List<String> dotWithSegmentList = Arrays.asList("a./", "a../", "a/.a", "a/..a");
        putAndGetObjectWithKeyList(dotWithSegmentList);
    }

    @Test
    void validObjectKeyTest() {
        List<String> dotWithoutSegmentList = Arrays.asList(".a", "..a", "a.", "a..");
        putAndGetObjectWithKeyList(dotWithoutSegmentList);

        List<String> diffLangList = Arrays.asList("中文测试", "日本語のテスト", "Λατινική δοκιμή", "Русский тест", "Zhōngwén cèshì",
                "한국어 시험", "ข้อสอบภาษาไทย", "épreuve de français");
        putAndGetObjectWithKeyList(diffLangList);

        List<String> specCharList = Arrays.asList("  !-_.*()/&$@=;:+ ,?\\{^}%`]>[~<#|'\"",
                "·～！@#¥%……^o^&*（）——【】「」、｜；：'，。/《》？");
        putAndGetObjectWithKeyList(specCharList);
    }

    private void putAndGetObjectWithKeyList(List<String> objectKeyList) {
        for (String s : objectKeyList) {
            Consts.LOG.info("Object Key is {}", s);
            InputStream content = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8));
            try {
                clearData(s);
                getHandler().putObject(PutObjectInput.builder()
                        .bucket(Consts.bucket)
                        .key(s)
                        .content(content)
                        .build());
                try (GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                        .bucket(Consts.bucket)
                        .key(s)
                        .build())) {
                    validateDataSame(sampleData, StringUtils.toString(got.getContent(), "content"));
                } catch (Exception e) {
                    testFailed(e);
                }
            } catch (Exception e) {
                testFailed(e);
            } finally {
                clearData(s);
            }
        }
    }

    @Test
    void basicObjectCRUDTest() {
        // basic crud
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getContentLength(), data.length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getVersionID(), putRes.getVersionID());
                validateDataSame(data, StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void nullObjectCRUDTest() {
        String key = getUniqueObjectKey();
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            try (GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())) {
                Assert.assertEquals(got.getContentLength(), 0);
                Assert.assertEquals(got.getEtag(), putRes.getEtag());
                Assert.assertEquals(got.getVersionID(), putRes.getVersionID());
                Assert.assertEquals(StringUtils.toString(got.getContent(), "content").length(), 0);
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void completeObjectCRUDTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        Date dt = Date.from(LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.UTC));
        String md5 = getContentMD5(data);
        ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                .contentLength(data.length())
                .contentMD5(md5)
                .cacheControl("max-age=600")
                .contentDisposition("attachment")
                .contentEncoding("deflate")
                .contentLanguage("en-US")
                .contentType("application/json")
                .expires(dt)
                .ssecAlgorithm(ssecAlgorithm)
                .ssecKey(ssecKey)
                .ssecKeyMD5(ssecKeyMD5)
//                .serverSideEncryption(ssecAlgorithm) incompatible with ssec
                .storageClass(StorageClassType.STORAGE_CLASS_IA)
                .build();
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            // use ssec, so we need to set header options to head/get an object
                            .ssecAlgorithm(ssecAlgorithm)
                            .ssecKey(ssecKey)
                            .ssecKeyMD5(ssecKeyMD5)
                            .build())
                    .build());
            Assert.assertEquals(headRes.getContentLength(), data.length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());
            Assert.assertEquals(headRes.getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getContentDisposition(), "attachment");
            Assert.assertEquals(headRes.getContentEncoding(), "deflate");
            Assert.assertEquals(headRes.getContentLanguage(), "en-US");
            Assert.assertEquals(headRes.getContentType(), "application/json");
            Assert.assertEquals(headRes.getExpiresInDate().toString(), dt.toString());
            Assert.assertEquals(headRes.getExpires(), DateConverter.dateToRFC1123String(dt));
            Assert.assertEquals(headRes.getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getSsecAlgorithm(), ssecAlgorithm);
            Assert.assertEquals(headRes.getSsecKeyMD5(), ssecKeyMD5);
            Assert.assertEquals(headRes.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);

            try (GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            // use ssec, so we need to set header options to head/get an object
                            .ssecAlgorithm(ssecAlgorithm)
                            .ssecKey(ssecKey)
                            .ssecKeyMD5(ssecKeyMD5)
                            .build())
                    .build())) {
                Assert.assertEquals(got.getContentLength(), data.length());
                Assert.assertEquals(got.getEtag(), putRes.getEtag());
                Assert.assertEquals(got.getVersionID(), putRes.getVersionID());
                Assert.assertEquals(got.getCacheControl(), "max-age=600");
                Assert.assertEquals(got.getContentDisposition(), "attachment");
                Assert.assertEquals(got.getContentEncoding(), "deflate");
                Assert.assertEquals(got.getContentLanguage(), "en-US");
                Assert.assertEquals(got.getContentType(), "application/json");
                Assert.assertEquals(got.getExpires(), DateConverter.dateToRFC1123String(dt));
                Assert.assertEquals(got.getExpiresInDate().toString(), dt.toString());
                Assert.assertEquals(got.getCacheControl(), "max-age=600");
                Assert.assertEquals(got.getSsecAlgorithm(), ssecAlgorithm);
                Assert.assertEquals(got.getSsecKeyMD5(), ssecKeyMD5);
                Assert.assertEquals(got.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
                validateDataSame(data, StringUtils.toString(got.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void CustomAndChineseHeaderTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        Map<String, String> custom = new HashMap<>(2);
        custom.put("custom1", "中文1");
        custom.put("custom2", "中文 空格");
        custom.put("custom3", "aabb-cc%20");
        custom.put("custom4", "aabb=cc%20");
        ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                .customMetadata(custom)
                .contentDisposition("attachment;filename=中文测试.txt")
                .build();
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getCustomMetadata().get("custom1"), "中文1");
            Assert.assertEquals(headRes.getCustomMetadata().get("custom2"), "中文 空格");
            Assert.assertEquals(headRes.getCustomMetadata().get("custom3"), "aabb-cc%20");
            Assert.assertEquals(headRes.getCustomMetadata().get("custom4"), "aabb=cc%20");
            Assert.assertEquals(headRes.getContentDisposition(), "attachment;filename=中文测试.txt");

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())) {
                Assert.assertEquals(headRes.getCustomMetadata().get("custom1"), "中文1");
                Assert.assertEquals(headRes.getCustomMetadata().get("custom2"), "中文 空格");
                Assert.assertEquals(headRes.getCustomMetadata().get("custom3"), "aabb-cc%20");
                Assert.assertEquals(headRes.getContentDisposition(), "attachment;filename=中文测试.txt");
                validateDataSame(data, StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void rangeGetObjectTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [1,31]
                    .options(ObjectMetaRequestOptions.builder().range(1, 31).build())
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), 31);
                validateDataSame(data.substring(1, 32), StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [1,31]
                    .range("bytes=1-31")
                    .options(ObjectMetaRequestOptions.builder().range(1, 32).build())
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), 31);
                validateDataSame(data.substring(1, 32), StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [-1,31]
                    .options(ObjectMetaRequestOptions.builder().range(-1, 31).build())
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), 32);
                validateDataSame(data.substring(0, 32), StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [1,-1] to the end
                    .options(ObjectMetaRequestOptions.builder().range(1, -1).build())
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), data.length() - 1);
                validateDataSame(data.substring(1, data.length()), StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [-1,-1] from start to the end
                    .options(ObjectMetaRequestOptions.builder().range(-1, -1).build())
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), data.length());
                validateDataSame(data, StringUtils.toString(getRes.getContent(), "content"));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void objectCRUDWithWrongParamsTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            String md5 = getContentMD5(data);
            String wrongMd5 = md5 + StringUtils.randomString(new Random().nextInt(3));
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .contentMD5(wrongMd5).build();
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(content)
                    .build());
        } catch (TosServerException e) {
            LOG.debug("error message: {}", e.getMessage());
            Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
            Assert.assertEquals(e.getCode(), Code.INVALID_DIGEST);
        }

        try {
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm("AES128") // wrong algorithm
                    .build();

        } catch (TosClientException e) {
            Assert.assertEquals(e.getMessage(), "invalid encryption-decryption algorithm");
        }

        try {
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .serverSideEncryption("AES128") // wrong algorithm
                    .build();

        } catch (TosClientException e) {
            Assert.assertEquals(e.getMessage(), "invalid serverSideEncryption input, only support AES256");
        }

        try {
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm(ssecAlgorithm)
                    .ssecKey(ssecKey)
                    .ssecKeyMD5(ssecKeyMD5)
                    .serverSideEncryption(ssecAlgorithm)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(content)
                    .build());
        } catch (TosServerException e) {
            LOG.debug("error message: {}", e.getMessage());
            Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
            Assert.assertEquals(e.getCode(), Code.INVALID_ARGUMENT);
        }

        try {
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm(ssecAlgorithm)
                    .ssecKey(ssecKey)
                    .ssecKeyMD5(ssecKeyMD5)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(content)
                    .build());
            getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            // use ssec, so we need to set header options to head/get an object
//                            .ssecAlgorithm(ssecAlgorithm)
//                            .ssecKey(ssecKey)
//                            .ssecKeyMD5(ssecKeyMD5)
                            .build())
                    .build());
        } catch (TosServerException e) {
            LOG.debug("error message: {}", e.getMessage());
            Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
//            Assert.assertEquals(e.getCode(), Code.INVALID_ARGUMENT);
        }
    }

    @Test
    void getObjectWithIfConditionTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        String putContentType = "application/json";
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            .contentType(putContentType)
                            .build())
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Date lastModified = headRes.getLastModifiedInDate();

            // 304 case
            // if-modified-since
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder().ifModifiedSince(lastModified).build())
                    .build())) {
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertTrue(e instanceof TosServerException);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_MODIFIED);
            }

            // if-none-math
            String etag = headRes.getEtag();
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder().ifNoneMatch(etag).build())
                    .build())) {
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertTrue(e instanceof TosServerException);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_MODIFIED);
            }

            // 412
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            .ifUnmodifiedSince(Date.from(lastModified.toInstant().minusSeconds(120)))
                            .build())
                    .build())) {
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.PRECONDITION_FAILED);
                Assert.assertEquals(e.getCode(), Code.PRECONDITION_FAILED);
            }

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .responseContentType("image/jpeg")
                    .build())) {
                Assert.assertNotEquals(getRes.getContentType(), putContentType);
                Assert.assertEquals(getRes.getContentType(), "image/jpeg");
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void objectCRUDInNotFoundBucketTest() {
        String notFoundBucket = "notfoundbucket-" + System.currentTimeMillis();
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(notFoundBucket)
                    .key(key)
                    .content(content)
                    .build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }

        try {
            getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(notFoundBucket)
                    .key(key)
                    .build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NOT_FOUND);
        }

        try {
            getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(notFoundBucket)
                    .key(key)
                    .build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }

        try {
            getHandler().deleteObject(DeleteObjectInput.builder()
                    .bucket(notFoundBucket)
                    .key(key)
                    .build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }
    }

    @Test
    void listObjectTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey();
        List<String> objectKeys = null;
        try {
            objectKeys = generateDataAndGetKeyList(12, uniquePrefix);
            String marker = null;
            boolean isTruncated = true;
            while (isTruncated) {
                ListObjectsV2Input input = ListObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .marker(marker)
                        .build();
                ListObjectsV2Output output = getHandler().listObjects(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 2);
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                List<ObjectTobeDeleted> objs = new ArrayList<>(objectKeys.size());
                for (String key : objectKeys) {
                    objs.add(ObjectTobeDeleted.builder().key(key).build());
                }
                DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .objects(objs)
                        .build();
                getHandler().deleteMultiObjects(input);
            }
        }
    }

    @Test
    void listObjectWithDelimiterTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey() + "/";
        String secondPath = "bbb/";
        String thirdPath = "ccc/";
        String forthPath = "ddd/";
        List<String> objectKeys = null;
        try {
            // aaa/
            objectKeys = generateDataAndGetKeyList(11, uniquePrefix);
            // aaa/bbb/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath));
            // aaa/bbb/ccc/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath + thirdPath));
            // aaa/bbb/ccc/ddd/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath + thirdPath + forthPath));


            String marker = null;
            boolean isTruncated = true;
            while (isTruncated) {
                ListObjectsV2Input input = ListObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath + forthPath)
                        .marker(marker)
                        .delimiter("/")
                        .build();
                ListObjectsV2Output output = getHandler().listObjects(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }

            marker = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsV2Input input = ListObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath)
                        .marker(marker)
                        .delimiter("/")
                        .build();
                ListObjectsV2Output output = getHandler().listObjects(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }

            marker = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsV2Input input = ListObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath)
                        .marker(marker)
                        .delimiter("/")
                        .build();
                ListObjectsV2Output output = getHandler().listObjects(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }

            marker = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsV2Input input = ListObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .marker(marker)
                        .delimiter("/")
                        .build();
                ListObjectsV2Output output = getHandler().listObjects(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                List<ObjectTobeDeleted> objs = new ArrayList<>(objectKeys.size());
                for (String key : objectKeys) {
                    objs.add(ObjectTobeDeleted.builder().key(key).build());
                }
                DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .objects(objs)
                        .build();
                getHandler().deleteMultiObjects(input);
            }
        }
    }

    @Test
    void listObjectType2Test() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey();
        List<String> objectKeys = null;
        try {
            objectKeys = generateDataAndGetKeyList(12, uniquePrefix);
            String startAfter = null;
            String continuationToken = null;
            boolean isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .fetchMeta(true)
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 2);
                }
                for (ListedObjectV2 item : output.getContents()) {
                    Assert.assertTrue(item.getMeta().size() > 0);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                List<ObjectTobeDeleted> objs = new ArrayList<>(objectKeys.size());
                for (String key : objectKeys) {
                    objs.add(ObjectTobeDeleted.builder().key(key).build());
                }
                DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .objects(objs)
                        .build();
                getHandler().deleteMultiObjects(input);
            }
        }
    }

    @Test
    void listObjectType2LoopTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey();
        List<String> objectKeys = null;
        try {
            objectKeys = generateDataAndGetKeyList(12, uniquePrefix);
            String startAfter = null;
            String continuationToken = null;
            boolean isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2UntilFinished(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 2);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                List<ObjectTobeDeleted> objs = new ArrayList<>(objectKeys.size());
                for (String key : objectKeys) {
                    objs.add(ObjectTobeDeleted.builder().key(key).build());
                }
                DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .objects(objs)
                        .build();
                getHandler().deleteMultiObjects(input);
            }
        }
    }

    @Test
    void listObjectType2WithDelimiterTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey() + "/";
        String secondPath = "bbb/";
        String thirdPath = "ccc/";
        String forthPath = "ddd/";
        List<String> objectKeys = null;
        try {
            // aaa/
            objectKeys = generateDataAndGetKeyList(11, uniquePrefix);
            // aaa/bbb/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath));
            // aaa/bbb/ccc/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath + thirdPath));
            // aaa/bbb/ccc/ddd/
            objectKeys.addAll(generateDataAndGetKeyList(11, uniquePrefix + secondPath + thirdPath + forthPath));


            String startAfter = null;
            String continuationToken = null;
            boolean isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath + forthPath)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .delimiter("/")
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }

            continuationToken = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .delimiter("/")
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }

            continuationToken = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .delimiter("/")
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }

            continuationToken = null;
            isTruncated = true;
            while (isTruncated) {
                ListObjectsType2Input input = ListObjectsType2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .startAfter(startAfter)
                        .continuationToken(continuationToken)
                        .delimiter("/")
                        .build();
                ListObjectsType2Output output = getHandler().listObjectsType2(input);
                Assert.assertNotNull(output.getContents());
                if (output.isTruncated()) {
                    Assert.assertEquals(output.getContents().size(), 5);
                } else {
                    Assert.assertEquals(output.getContents().size(), 1);
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                List<ObjectTobeDeleted> objs = new ArrayList<>(objectKeys.size());
                for (String key : objectKeys) {
                    objs.add(ObjectTobeDeleted.builder().key(key).build());
                }
                DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                        .bucket(Consts.bucket)
                        .objects(objs)
                        .build();
                getHandler().deleteMultiObjects(input);
            }
        }
    }

    @Test
    void listObjectVersionsTest() {
        String uniqueKey = internalObjectListVersionsPrefix + getUniqueObjectKey();
        try {
            generateDataWithSameKey(3, uniqueKey);
            String keyMarker = null;
            String versionIdMarker = null;
            boolean isTruncated = true;
            int versionsCount = 0, deleteMarkersCount = 0;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniqueKey)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                Assert.assertNotNull(output.getVersions());
                versionsCount += output.getVersions().size();
                Assert.assertNotNull(output.getDeleteMarkers());
                deleteMarkersCount += output.getDeleteMarkers().size();
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 6);
            Assert.assertEquals(deleteMarkersCount, 3);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void listObjectVersionsWithSubDirTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey();
        String secondPath = "/bbb";
        String thirdPath = "/ccc";
        String forthPath = "/ddd";
        try {
            // aaa/
            generateDataWithSameKey(1, uniquePrefix);
            // aaa/bbb/
            generateDataWithSameKey(2, uniquePrefix + secondPath);
            // aaa/bbb/ccc/
            generateDataWithSameKey(3, uniquePrefix + secondPath + thirdPath);
            // aaa/bbb/ccc/ddd/
            generateDataWithSameKey(4, uniquePrefix + secondPath + thirdPath + forthPath);

            String keyMarker = null;
            String versionIdMarker = null;
            boolean isTruncated = true;
            int versionsCount = 0, deleteMarkersCount = 0;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath + forthPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                if (output.getVersions() != null) {
                    versionsCount += output.getVersions().size();
                }
                if (output.getDeleteMarkers() != null) {
                    deleteMarkersCount += output.getDeleteMarkers().size();
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
                Assert.assertNull(output.getCommonPrefixes());
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 8);
            Assert.assertEquals(deleteMarkersCount, 4);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                if (output.getVersions() != null) {
                    versionsCount += output.getVersions().size();
                }
                if (output.getDeleteMarkers() != null) {
                    deleteMarkersCount += output.getDeleteMarkers().size();
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }

            // 写两次，删一次
            Assert.assertEquals(versionsCount, 14);
            Assert.assertEquals(deleteMarkersCount, 7);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                if (output.getVersions() != null) {
                    versionsCount += output.getVersions().size();
                }
                if (output.getDeleteMarkers() != null) {
                    deleteMarkersCount += output.getDeleteMarkers().size();
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 18);
            Assert.assertEquals(deleteMarkersCount, 9);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                if (output.getVersions() != null) {
                    versionsCount += output.getVersions().size();
                }
                if (output.getDeleteMarkers() != null) {
                    deleteMarkersCount += output.getDeleteMarkers().size();
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 20);
            Assert.assertEquals(deleteMarkersCount, 10);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void listObjectVersionsWithDelimiterTest() {
        String uniquePrefix = internalObjectListPrefix + getUniqueObjectKey();
        String secondPath = "/bbb";
        String thirdPath = "/ccc";
        String forthPath = "/ddd";
        try {
            // aaa/
            generateDataWithSameKey(3, uniquePrefix);
            // aaa/bbb/
            generateDataWithSameKey(3, uniquePrefix + secondPath);
            // aaa/bbb/ccc/
            generateDataWithSameKey(3, uniquePrefix + secondPath + thirdPath);
            // aaa/bbb/ccc/ddd/
            generateDataWithSameKey(3, uniquePrefix + secondPath + thirdPath + forthPath);

            String keyMarker = null;
            String versionIdMarker = null;
            boolean isTruncated = true;
            int versionsCount = 0, deleteMarkersCount = 0;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath + forthPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .delimiter("/")
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                Assert.assertNotNull(output.getVersions());
                versionsCount += output.getVersions().size();
                Assert.assertNotNull(output.getDeleteMarkers());
                deleteMarkersCount += output.getDeleteMarkers().size();
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
                Assert.assertNull(output.getCommonPrefixes());
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 6);
            Assert.assertEquals(deleteMarkersCount, 3);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath + thirdPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .delimiter("/")
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                Assert.assertNotNull(output.getVersions());
                versionsCount += output.getVersions().size();
                Assert.assertNotNull(output.getDeleteMarkers());
                deleteMarkersCount += output.getDeleteMarkers().size();
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }

            // 写两次，删一次
            Assert.assertEquals(versionsCount, 6);
            Assert.assertEquals(deleteMarkersCount, 3);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix + secondPath)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .delimiter("/")
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                Assert.assertNotNull(output.getVersions());
                versionsCount += output.getVersions().size();
                Assert.assertNotNull(output.getDeleteMarkers());
                deleteMarkersCount += output.getDeleteMarkers().size();
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
            // 写两次，删一次
            Assert.assertEquals(versionsCount, 6);
            Assert.assertEquals(deleteMarkersCount, 3);

            keyMarker = null;
            versionIdMarker = null;
            versionsCount = 0;
            deleteMarkersCount = 0;
            isTruncated = true;
            while (isTruncated) {
                ListObjectVersionsV2Input input = ListObjectVersionsV2Input.builder()
                        .bucket(Consts.bucket)
                        .maxKeys(5)
                        .prefix(uniquePrefix)
                        .keyMarker(keyMarker)
                        .versionIDMarker(versionIdMarker)
                        .delimiter("/")
                        .build();
                ListObjectVersionsV2Output output = getHandler().listObjectVersions(input);
                Assert.assertNotNull(output.getVersions());
                versionsCount += output.getVersions().size();
                Assert.assertNotNull(output.getDeleteMarkers());
                deleteMarkersCount += output.getDeleteMarkers().size();
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void setObjectMetaTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        Date dt = Date.from(LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.UTC));
        String md5 = getContentMD5(data);
        Map<String, String> customMeta = new HashMap<>(2);
        customMeta.put("tag", "111");
        customMeta.put("spec", "cd()*#@*+");
        ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                .contentLength(data.length())
                .contentMD5(md5)
                .cacheControl("max-age=600")
                .contentDisposition("attachment")
                .contentEncoding("deflate")
                .contentLanguage("en-US")
                .expires(dt)
                .customMetadata(customMeta)
                // the following settings do not affect to the existed object
                .ssecAlgorithm(ssecAlgorithm)
                .ssecKey(ssecKey)
                .ssecKeyMD5(ssecKeyMD5)
//                .serverSideEncryption(ssecAlgorithm) incompatible with ssec
                .storageClass(StorageClassType.STORAGE_CLASS_IA)
                .build();
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .options(new ObjectMetaRequestOptions().setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD))
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            // default content-type set by sdk
            Assert.assertEquals(headRes.getContentType(), "binary/octet-stream");
            Assert.assertNull(headRes.getCacheControl());
            Assert.assertNull(headRes.getContentEncoding());
            Assert.assertNull(headRes.getCustomMetadata());
            Assert.assertNull(headRes.getContentLanguage());

            // set object meta
            SetObjectMetaInput input = SetObjectMetaInput.builder().options(options).bucket(bucket).key(key).build();
            getHandler().setObjectMeta(input);

            headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getContentLength(), data.length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());
            Assert.assertEquals(headRes.getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getContentDisposition(), "attachment");
            Assert.assertEquals(headRes.getContentEncoding(), "deflate");
            Assert.assertEquals(headRes.getContentLanguage(), "en-US");
            Assert.assertEquals(headRes.getContentType(), "binary/octet-stream");
            Assert.assertEquals(headRes.getExpiresInDate().toString(), dt.toString());
            Assert.assertEquals(headRes.getExpires(), DateConverter.dateToRFC1123String(dt));
            Assert.assertEquals(headRes.getCacheControl(), "max-age=600");
            Assert.assertNotNull(headRes.getCustomMetadata());
            Assert.assertEquals(headRes.getCustomMetadata().size(), 2);
            Assert.assertEquals(headRes.getCustomMetadata().get("tag"), "111");
            Assert.assertEquals(headRes.getCustomMetadata().get("spec"), "cd()*#@*+");
            // can not set ssec to the existed object
            Assert.assertNull(headRes.getSsecAlgorithm());
            Assert.assertNull(headRes.getSsecKeyMD5());
            // can not change storage class to the existed object
            Assert.assertEquals(headRes.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void objectACLTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            List<GrantV2> grants = new ArrayList<>(2);
            grants.add(GrantV2.builder()
                    .grantee(GranteeV2.builder()
                            .type(GranteeType.GRANTEE_GROUP)
                            .canned(CannedType.CANNED_ALL_USERS)
                            .build())
                    .permission(PermissionType.PERMISSION_READ)
                    .build());
            grants.add(GrantV2.builder()
                    .grantee(GranteeV2.builder()
                            .id("123")
                            .displayName("volc")
                            .type(GranteeType.GRANTEE_USER)
                            .build())
                    .permission(PermissionType.PERMISSION_WRITE)
                    .build());
            PutObjectACLInput input = PutObjectACLInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .grants(grants)
                    .owner(Owner.builder().id("volc-test").build())
                    .bucketOwnerEntrusted(true)
                    .build();
            getHandler().putObjectAcl(input);

            GetObjectACLV2Input get = GetObjectACLV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            GetObjectACLV2Output out = getHandler().getObjectAcl(get);
            Assert.assertTrue(out.isBucketOwnerEntrusted());
            Assert.assertNotNull(out.getGrants());
            Assert.assertEquals(out.getGrants().size(), 2);
            for (GrantV2 g : out.getGrants()) {
                if (g.getPermission() == PermissionType.PERMISSION_WRITE) {
                    Assert.assertEquals(g.getGrantee().getType(), GranteeType.GRANTEE_USER);
                    Assert.assertEquals(g.getGrantee().getId(), "123");
                    Assert.assertEquals(g.getGrantee().getDisplayName(), "volc");
                } else {
                    Assert.assertEquals(g.getPermission(), PermissionType.PERMISSION_READ);
                    Assert.assertEquals(g.getGrantee().getType(), GranteeType.GRANTEE_GROUP);
                    Assert.assertEquals(g.getGrantee().getCanned(), CannedType.CANNED_ALL_USERS);
                }
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void getFileStatusTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        int getCount = 0;
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            GetFileStatusInput input = GetFileStatusInput.builder().bucket(Consts.bucket).key(key).build();
            GetFileStatusOutput out = getHandler().getFileStatus(input);
            Assert.assertEquals(out.getKey(), key);
            Assert.assertEquals(out.getSize(), data.length());
            Assert.assertNotNull(out.getLastModified());
            Assert.assertNotNull(out.getCrc32());
            Assert.assertNotNull(out.getCrc64());
            getCount++;

            input.setKey(key.substring(0, key.length() - 1));
            GetFileStatusOutput output = getHandler().getFileStatus(input);
            Assert.assertNull(output);
            getCount++;
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), 404);
            Assert.assertEquals(getCount, 1);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void deleteMultiObjectsTest() {
        List<String> objectKeys = null;
        try {
            objectKeys = generateDataAndGetKeyList(10, internalObjectCrudPrefix);
            List<ObjectTobeDeleted> delObjs = new ArrayList<>(objectKeys.size());
            for (String key : objectKeys) {
                delObjs.add(ObjectTobeDeleted.builder().key(key).build());
            }
            DeleteMultiObjectsV2Input input = DeleteMultiObjectsV2Input.builder()
                    .objects(delObjs)
                    .bucket(bucket)
                    .build();
            DeleteMultiObjectsV2Output output = getHandler().deleteMultiObjects(input);
            Assert.assertNotNull(output.getDeleteds());
            Assert.assertNull(output.getErrors());
            Assert.assertEquals(output.getDeleteds().size(), delObjs.size());

            // delete quiet
            objectKeys = generateDataAndGetKeyList(10, internalObjectCrudPrefix);
            delObjs = new ArrayList<>(objectKeys.size());
            for (String key : objectKeys) {
                delObjs.add(ObjectTobeDeleted.builder().key(key).build());
            }
            input = DeleteMultiObjectsV2Input.builder()
                    .objects(delObjs)
                    .bucket(bucket)
                    .quiet(true)
                    .build();
            output = getHandler().deleteMultiObjects(input);
            Assert.assertNull(output.getDeleteds());
            Assert.assertNull(output.getErrors());

            // delete with version
            String key = internalObjectCrudPrefix + System.nanoTime();
            objectKeys = generateDataWithSameKey(10, key);
            delObjs = new ArrayList<>(objectKeys.size());
            for (int i = 0; i < objectKeys.size(); i++) {
                if (i % 2 == 0) {
                    delObjs.add(ObjectTobeDeleted.builder().key(key).versionID("notexistversionid").build());
                } else {
                    delObjs.add(ObjectTobeDeleted.builder().key(key).versionID(objectKeys.get(i)).build());
                }
            }
            input = DeleteMultiObjectsV2Input.builder()
                    .objects(delObjs)
                    .bucket(bucket)
                    .build();
            output = getHandler().deleteMultiObjects(input);
            Assert.assertNotNull(output.getDeleteds());
            // not found versionId not return error
            Assert.assertNull(output.getErrors());
            Assert.assertEquals(output.getDeleteds().size(), delObjs.size());
        } catch (Exception e) {
            testFailed(e);
        } finally {
            if (objectKeys != null) {
                for (String key : objectKeys) {
                    getHandler().deleteObject(DeleteObjectInput.builder().bucket(bucket).key(key).build());
                }
            }
        }
    }

    @Test
    void getObjectWithCrcTest() {
        // basic crud
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getContentLength(), data.length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getVersionID(), putRes.getVersionID());
                // consume the inputStream, it will calculate crc64 auto.
                String readData = StringUtils.toString(getRes.getContent(), "content");
                validateDataSame(data, readData);
            } catch (IOException e) {
                testFailed(e);
            }
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .range(0, 127).build();
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), 128);
                Assert.assertFalse(getRes.getContent() instanceof CheckedInputStream);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void putObjectWithRateLimiterTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            long start = System.currentTimeMillis();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .rateLimiter(new DefaultRateLimiter(16 * 1024))
                    .content(content)
                    .build());
            long end = System.currentTimeMillis();
            LOG.info("putObject cost {} ms", end - start);
            Assert.assertTrue((end - start) > 7000);

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getContentLength(), data.length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());

            start = System.currentTimeMillis();
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .rateLimiter(new DefaultRateLimiter(10 * 1024))
                    .build());
                 InputStream stream = getRes.getContent();
                 ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getVersionID(), putRes.getVersionID());
                byte[] tmp = new byte[1024];
                int once = 0;
                while ((once = stream.read(tmp)) != -1) {
                    result.write(tmp, 0, once);
                }
                validateDataSame(data, result.toString());
                end = System.currentTimeMillis();
                LOG.info("getObject cost {} ms", end - start);
                Assert.assertTrue((end - start) > 10000);
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void putObjectWithTrafficLimitTest() {
        String key = getUniqueObjectKey();
        try (InputStream content = new FileInputStream(sampleFilePath)) {
            long start = System.currentTimeMillis();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(new ObjectMetaRequestOptions().setTrafficLimit(800 * 1024 * 8))
                    .content(content)
                    .build());
            long end = System.currentTimeMillis();
            LOG.info("putObject cost {} ms", end - start);
            Assert.assertTrue((end - start) > 12000);

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getContentLength(), new File(sampleFilePath).length());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());

            start = System.currentTimeMillis();
            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(new ObjectMetaRequestOptions().setTrafficLimit(800 * 1024 * 8))
                    .build());
                 InputStream stream = getRes.getContent()) {
                Assert.assertEquals(getRes.getContentLength(), new File(sampleFilePath).length());
                Assert.assertEquals(getRes.getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getVersionID(), putRes.getVersionID());

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = stream.read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
                end = System.currentTimeMillis();
                LOG.info("getObject cost {} ms", end - start);
                Assert.assertTrue((end - start) > 12000);
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void putObjectWithNetworkStreamTest() {
        // basic crud
        String key = getUniqueObjectKey();
        String dataUrl = "https://www.volcengine.com/docs/6349/79895";
        try (BufferedInputStream content = new BufferedInputStream(new URL(dataUrl).openStream())) {
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .readLimit(1024 * 1024)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertEquals(headRes.getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getVersionID(), putRes.getVersionID());

            try (GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())) {
                Assert.assertEquals(getRes.getContentLength(), headRes.getContentLength());
                Assert.assertEquals(getRes.getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getVersionID(), putRes.getVersionID());
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    @Test
    void objectTaggingTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            try {
                getHandler().deleteObjectTagging(new DeleteObjectTaggingInput().setBucket(bucket).setKey(key));
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());

            Tag tag1 = new Tag().setKey("1").setValue("2");
            Tag tag2 = new Tag().setKey("2").setValue("1");
            TagSet tagSet = new TagSet();
            tagSet.setTags(Arrays.asList(tag1, tag2));

            getHandler().putObjectTagging(new PutObjectTaggingInput().setBucket(bucket).setKey(key).setTagSet(tagSet));

            GetObjectTaggingOutput got = getHandler().getObjectTagging(new GetObjectTaggingInput().setBucket(bucket).setKey(key));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getTagSet());
            Assert.assertNotNull(got.getTagSet().getTags());
            Assert.assertEquals(got.getTagSet().getTags().size(), 2);
            List<Tag> tags = got.getTagSet().getTags();
            boolean matched = false;
            for (Tag tag : tags) {
                if (tag.getKey().equals("1")) {
                    matched = true;
                    Assert.assertEquals(tag.getValue(), "2");
                }
                if (tag.getKey().equals("2")) {
                    matched = true;
                    Assert.assertEquals(tag.getValue(), "1");
                }
            }
            Assert.assertTrue(matched);

            getHandler().deleteObjectTagging(new DeleteObjectTaggingInput().setBucket(bucket).setKey(key));
            try {
                getHandler().getObjectTagging(new GetObjectTaggingInput().setBucket(bucket).setKey(key));
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try {
                getHandler().deleteObjectTagging(new DeleteObjectTaggingInput().setBucket(bucket).setKey(key));
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void callbackMockTest() {
        // putObject
        MockWebServer server = new MockWebServer();
        String callbackResult = "call success";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(callbackResult)
                .setHeader(TosHeader.HEADER_CRC64, "0"));

        try {
            server.start();
            String endpoint = server.getHostName() + ":" + server.getPort();
            TosObjectRequestHandler handler = ClientInstance.getObjectRequestHandlerWithPathStyleInstance(
                    new TosRequestFactory(null, endpoint).setScheme("http"));
            PutObjectOutput output = handler.putObject(new PutObjectInput()
                    .setBucket("aaa").setKey("bb").setCallback("cc").setCallbackVar("dd"));
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK), "cc");
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK_VAR), "dd");
            Assert.assertEquals(output.getCallbackResult(), callbackResult);
            server.shutdown();
        } catch (TosException | IOException | InterruptedException e) {
            testFailed(e);
        }

        // completeUpload
        server = new MockWebServer();
        String etag = "etag1";
        String location = "location1";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(callbackResult)
                .setHeader(TosHeader.HEADER_ETAG, etag)
                .setHeader(TosHeader.HEADER_LOCATION, location));
        String callbackResultInJson = "{\"test-key\": \"test-value\"}";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(callbackResultInJson)
                .setHeader(TosHeader.HEADER_ETAG, etag)
                .setHeader(TosHeader.HEADER_LOCATION, location));

        try {
            server.start();
            String endpoint = server.getHostName() + ":" + server.getPort();
            TosObjectRequestHandler handler = ClientInstance.getObjectRequestHandlerWithPathStyleInstance(
                    new TosRequestFactory(null, endpoint).setScheme("http"));
            CompleteMultipartUploadV2Output output = handler.completeMultipartUpload(new
                    CompleteMultipartUploadV2Input().setBucket("aaa").setKey("bb").setCompleteAll(true)
                    .setUploadID("ccc").setCallback("ddd").setCallbackVar("eee"));
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK), "ddd");
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK_VAR), "eee");
            Assert.assertEquals(output.getCallbackResult(), callbackResult);
            Assert.assertEquals(output.getEtag(), etag);
            Assert.assertEquals(output.getLocation(), location);

            output = handler.completeMultipartUpload(new CompleteMultipartUploadV2Input().setBucket("aaa")
                    .setKey("bb").setCompleteAll(true).setUploadID("ccc").setCallback("ddd").setCallbackVar("eee"));
            request = server.takeRequest();
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK), "ddd");
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CALLBACK_VAR), "eee");
            Assert.assertEquals(output.getCallbackResult(), callbackResultInJson);
            Assert.assertEquals(output.getEtag(), etag);
            Assert.assertEquals(output.getLocation(), location);
            server.shutdown();
        } catch (TosException | IOException | InterruptedException e) {
            testFailed(e);
        }
    }

    @Test
    void seekLargeObjectTest() {
        String data = StringUtils.randomString(16 << 20);
        String key = "object-large-" + System.currentTimeMillis();
        try {
            getHandler().putObject(new PutObjectInput().setBucket(bucket).setKey(key).setContentLength(16 << 20)
                    .setContent(new ByteArrayInputStream(data.getBytes())));
        } catch (TosException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }

        try (GetObjectV2Output object = getHandler().getObject(new GetObjectV2Input().setBucket(bucket).setKey(key))) {
            Assert.assertNotNull(object.getContent());
            InputStream inputStream = object.getContent();
            byte[] tmp = new byte[1024];
            int n = inputStream.read(tmp);
            Assert.assertEquals(n, 1024);
            long start = System.currentTimeMillis();
            // 只读一点 body 数据，立即 close
            object.forceClose();
            long end = System.currentTimeMillis();
            LOG.info("close stream immediately, {} ms.", end - start);
            Assert.assertTrue(end - start <= 10);
        } catch (TosException | IOException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try {
                getHandler().deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(key));
            } catch (TosException e) {
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void restoreObjectTest() {
        // basic crud
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .options(ObjectMetaRequestOptions.builder()
                            // 冷归档对象
                            .storageClass(StorageClassType.STORAGE_CLASS_COLD_ARCHIVE)
                            .build())
                    .build());

            try {
                getHandler().headObject(HeadObjectV2Input.builder()
                        .bucket(Consts.bucket)
                        .key(key)
                        .build());
            } catch (TosServerException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
                Assert.assertEquals(e.getCode(), Code.INVALID_OBJECT_STATE);
            }

            // 归档解冻
            try {
                // invalid days
                getHandler().restoreObject(new RestoreObjectInput().setBucket(bucket).setKey(key).setDays(366));
            } catch (TosServerException e) {
                e.printStackTrace();
                Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
                Assert.assertEquals(e.getCode(), Code.INVALID_ARGUMENT);
            }
            RestoreObjectOutput output = getHandler().restoreObject(
                    new RestoreObjectInput().setBucket(bucket).setKey(key).setDays(1)
                            .setRestoreJobParameters(new RestoreJobParameters().setTier(TierType.TIER_EXPEDITED)));
            Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            testFailed(e);
        } finally {
            clearData(key);
        }
    }

    private List<String> generateDataAndGetKeyList(int num, String pre) {
        List<String> keys = new ArrayList<>(num);
        Map<String, String> meta = new HashMap<>();
        meta.put("key1", "value1");
        for (int i = 0; i < num; i++) {
            String keyWithIdx = pre + i;
            PutObjectInput input = PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(keyWithIdx)
                    .content(new ByteArrayInputStream(sampleData.getBytes()))
                    .options(new ObjectMetaRequestOptions().setCustomMetadata(meta))
                    .build();
            getHandler().putObject(input);
            keys.add(keyWithIdx);
        }
        return keys;
    }

    private List<String> generateDataWithSameKey(int num, String key) {
        // 覆盖2次，删除1次
        List<String> versions = new ArrayList<>(3 * num);
        for (int i = 0; i < num; i++) {
            PutObjectOutput output = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(new ByteArrayInputStream(sampleData.getBytes()))
                    .build());
            versions.add(output.getVersionID());
            output = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(new ByteArrayInputStream(sampleData.getBytes()))
                    .build());
            versions.add(output.getVersionID());
            DeleteObjectOutput del = getHandler().deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(key));
            versions.add(del.getVersionID());
        }
        return versions;
    }

    private String getUniqueObjectKey() {
        return StringUtils.randomString(10);
    }

    protected static String getContentMD5(String data) {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
            md5 = new String(Base64.encodeBase64(bytes));
        } catch (NoSuchAlgorithmException e) {
            testFailed(e);
        }
        return md5;
    }

    private void validateDataSame(String src, String dst) {
        // for small size data compare
        if (src == null || dst == null) {
            testFailed(new NullPointerException("input is null"));
        }
        if (src.length() != dst.length()) {
            Consts.LOG.error("data length not same, src is {}, dst is {}", src.length(), dst.length());
            Assert.fail();
        }
        if (!StringUtils.equals(src, dst)) {
            Consts.LOG.error("data not same, src is {}, dst is {}", src, dst);
            Assert.fail();
        }
    }

    private String getMD5() {
        if (sampleFileMD5 != null) {
            return sampleFileMD5;
        }
        synchronized (this) {
            try (FileInputStream fileInputStream = new FileInputStream(sampleFilePath)) {
                MessageDigest MD5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = fileInputStream.read(buffer)) != -1) {
                    MD5.update(buffer, 0, length);
                }
                sampleFileMD5 = new String(Hex.encodeHex(MD5.digest()));
            } catch (Exception e) {
                testFailed(e);
            }
            return sampleFileMD5;
        }
    }

    private void clearData(String key) {
        getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
    }

    private static void testFailed(Exception e) {
        Consts.LOG.error("object test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
