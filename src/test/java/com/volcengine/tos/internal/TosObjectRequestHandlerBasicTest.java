package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.UnexpectedStatusCodeException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.CannedType;
import com.volcengine.tos.comm.common.GranteeType;
import com.volcengine.tos.comm.common.PermissionType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.GranteeV2;
import com.volcengine.tos.model.acl.Owner;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.internal.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.volcengine.tos.Consts.*;

public class TosObjectRequestHandlerBasicTest {
    private static final String sampleData = StringUtils.randomString(128 << 10);
    private static final String ssecKey = "Y2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2M=";
    private static final String ssecKeyMD5 = "ACdH+Fu9K3HlXdIUBu8GdA==";
    private static final String ssecAlgorithm = "AES256";

    private TosObjectRequestHandler getHandler() {
        return ClientInstance.getObjectRequestHandlerInstance();
    }

    @BeforeTest
    void init() throws InterruptedException {
        try{
            HeadBucketV2Output head = ClientInstance.getBucketRequestHandlerInstance()
                    .headBucket(HeadBucketV2Input.builder().bucket(Consts.bucket).build());
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try{
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
        try{
            HeadBucketV2Output head = ClientInstance.getBucketRequestHandlerInstance()
                    .headBucket(HeadBucketV2Input.builder().bucket(Consts.bucketCopy).build());
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try{
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
        char[] invisibleChars = new char[32];
        for (int i = 0; i < 32; ++i) {
            invisibleChars[i] = (char) i;
        }
        String invisibleString = Arrays.toString(invisibleChars);
        try {
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucketCopy).key(invisibleString).build();
            getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "object key should not contain invisible unicode characters");
        }

        invisibleChars = new char[128];
        for (int i = 0; i < 128; ++i) {
            invisibleChars[i] = (char) (i+128);
        }
        invisibleString = Arrays.toString(invisibleChars);
        try {
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(invisibleString).build();
            getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "object key should not contain invisible unicode characters");
        }


        // invalid length
        String longLengthObjectKey = StringUtils.randomString(697);
        List<String> invalidKeyList = Arrays.asList(null, "", longLengthObjectKey);
        for (String key : invalidKeyList) {
            try {
                PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(key).build();
                getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
                Assert.fail();
            } catch (IllegalArgumentException e) {
                Assert.assertEquals(e.getMessage(), "invalid object name, the length must be [1,696]");
            }
        }

        // invalid / \\
        invalidKeyList = Arrays.asList("\\aaa", "/aa");
        for (String key : invalidKeyList) {
            try {
                PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(key).build();
                getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
            } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "invalid object name, the object name can not start with / or \\");
            }
        }

        invalidKeyList = Arrays.asList(".", "..", "%2e", "%2e%2e", "%2E", "%2E%2E", ".%2e", "%2e.", ".%2E", "%2E.");
        for (String key : invalidKeyList) {
            try {
                PutObjectBasicInput basicInput = PutObjectBasicInput.builder().bucket(Consts.bucket).key(key).build();
                getHandler().putObject(PutObjectInput.builder().putObjectBasicInput(basicInput).build());
            } catch (IllegalArgumentException e) {
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
                PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                        .bucket(Consts.bucket)
                        .key(s)
                        .build();
                getHandler().putObject(PutObjectInput.builder()
                        .putObjectBasicInput(basicInput)
                        .content(content)
                        .build());
                try(GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                        .bucket(Consts.bucket)
                        .key(s)
                        .build())){
                    validateDataSame(sampleData, StringUtils.toString(got.getContent()));
                } catch (Exception e) {
                    testFailed(e);
                }
            } catch (Exception e) {
                testFailed(e);
            } finally{
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
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentLength(), data.length());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getVersionID(), putRes.getVersionID());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentLength(), data.length());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getEtag(), putRes.getEtag());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getVersionID(), putRes.getVersionID());
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            clearData(key);
        }
    }

    @Test
    void nullObjectCRUDTest() {
        String key = getUniqueObjectKey();
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .build());
            try(GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())){
                Assert.assertNotNull(got.getGetObjectBasicOutput());
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentLength(), 0);
                Assert.assertEquals(got.getGetObjectBasicOutput().getEtag(), putRes.getEtag());
                Assert.assertEquals(got.getGetObjectBasicOutput().getVersionID(), putRes.getVersionID());
                Assert.assertEquals(StringUtils.toString(got.getContent()).length(), 0);
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
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
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
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
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentLength(), data.length());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getVersionID(), putRes.getVersionID());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentDisposition(), "attachment");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentEncoding(), "deflate");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentLanguage(), "en-US");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentType(), "application/json");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getExpiresInDate().toString(), dt.toString());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getExpires(), DateConverter.dateToRFC1123String(dt));
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getSsecAlgorithm(), ssecAlgorithm);
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getSsecKeyMD5(), ssecKeyMD5);
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getStorageClass(), StorageClassType.STORAGE_CLASS_IA);

            try(GetObjectV2Output got = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            // use ssec, so we need to set header options to head/get an object
                            .ssecAlgorithm(ssecAlgorithm)
                            .ssecKey(ssecKey)
                            .ssecKeyMD5(ssecKeyMD5)
                            .build())
                    .build())){
                Assert.assertNotNull(got.getGetObjectBasicOutput());
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentLength(), data.length());
                Assert.assertEquals(got.getGetObjectBasicOutput().getEtag(), putRes.getEtag());
                Assert.assertEquals(got.getGetObjectBasicOutput().getVersionID(), putRes.getVersionID());
                Assert.assertEquals(got.getGetObjectBasicOutput().getCacheControl(), "max-age=600");
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentDisposition(), "attachment");
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentEncoding(), "deflate");
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentLanguage(), "en-US");
                Assert.assertEquals(got.getGetObjectBasicOutput().getContentType(), "application/json");
                Assert.assertEquals(got.getGetObjectBasicOutput().getExpires(), DateConverter.dateToRFC1123String(dt));
                Assert.assertEquals(got.getGetObjectBasicOutput().getExpiresInDate().toString(), dt.toString());
                Assert.assertEquals(got.getGetObjectBasicOutput().getCacheControl(), "max-age=600");
                Assert.assertEquals(got.getGetObjectBasicOutput().getSsecAlgorithm(), ssecAlgorithm);
                Assert.assertEquals(got.getGetObjectBasicOutput().getSsecKeyMD5(), ssecKeyMD5);
                Assert.assertEquals(got.getGetObjectBasicOutput().getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
                validateDataSame(data, StringUtils.toString(got.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
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
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom1"), "中文1");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom2"), "中文 空格");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom3"), "aabb-cc%20");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom4"), "aabb=cc%20");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentDisposition(), "attachment;filename=中文测试.txt");

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom1"), "中文1");
                Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom2"), "中文 空格");
                Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("custom3"), "aabb-cc%20");
                Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentDisposition(), "attachment;filename=中文测试.txt");
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            clearData(key);
        }
    }

    @Test
    void rangeGetObjectTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [1,31]
                    .options(ObjectMetaRequestOptions.builder().range(1,31).build())
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentLength(), 31);
                validateDataSame(data.substring(1,32), StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [-1,31]
                    .options(ObjectMetaRequestOptions.builder().range(-1,31).build())
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentLength(), 32);
                validateDataSame(data.substring(0,32), StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [1,-1] to the end
                    .options(ObjectMetaRequestOptions.builder().range(1,-1).build())
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentLength(), data.length()-1);
                validateDataSame(data.substring(1, data.length()), StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    // [-1,-1] from start to the end
                    .options(ObjectMetaRequestOptions.builder().range(-1,-1).build())
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentLength(), data.length());
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            clearData(key);
        }
    }

    @Test
    void objectCRUDWithWrongParamsTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try{
            String md5 = getContentMD5(data);
            String wrongMd5 = md5 + StringUtils.randomString(new Random().nextInt(3));
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .contentMD5(wrongMd5).build();
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());
        } catch (TosServerException e) {
            LOG.debug("error message: {}", e.getMessage());
            Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
            Assert.assertEquals(e.getCode(), Code.INVALID_DIGEST);
        }

        try{
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm("AES128") // wrong algorithm
                    .build();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "invalid encryption-decryption algorithm");
        }

        try{
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .serverSideEncryption("AES128") // wrong algorithm
                    .build();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "invalid serverSideEncryption input, only support AES256");
        }

        try{
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm(ssecAlgorithm)
                    .ssecKey(ssecKey)
                    .ssecKeyMD5(ssecKeyMD5)
                    .serverSideEncryption(ssecAlgorithm)
                    .build();
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());
        } catch (TosServerException e) {
            LOG.debug("error message: {}", e.getMessage());
            Assert.assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
            Assert.assertEquals(e.getCode(), Code.INVALID_ARGUMENT);
        }

        try{
            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                    .ssecAlgorithm(ssecAlgorithm)
                    .ssecKey(ssecKey)
                    .ssecKeyMD5(ssecKeyMD5)
                    .build();
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
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
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            .contentType(putContentType)
                            .build())
                    .build();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Date lastModified = headRes.getHeadObjectBasicOutput().getLastModifiedInDate();

            // 304 case
            // if-modified-since
            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder().ifModifiedSince(lastModified).build())
                    .build())){
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertTrue(e instanceof UnexpectedStatusCodeException);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_MODIFIED);
            }

            // if-none-math
            String etag = headRes.getHeadObjectBasicOutput().getEtag();
            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder().ifNoneMatch(etag).build())
                    .build())){
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertTrue(e instanceof UnexpectedStatusCodeException);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_MODIFIED);
            }

            // 412
            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(ObjectMetaRequestOptions.builder()
                            .ifUnmodifiedSince(Date.from(lastModified.toInstant().minusSeconds(120)))
                            .build())
                    .build())){
            } catch (IOException e) {
                testFailed(e);
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.PRECONDITION_FAILED);
                Assert.assertEquals(e.getCode(), Code.PRECONDITION_FAILED);
            }

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .responseContentType("image/jpeg")
                    .build())){
                Assert.assertNotNull(getRes.getGetObjectBasicOutput());
                // todo it does not work
//                Assert.assertNotEquals(getRes.getGetObjectBasicOutput().getContentType(), putContentType);
//                Assert.assertEquals(getRes.getGetObjectBasicOutput().getContentType(), "image/jpeg");
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            clearData(key);
        }
    }

    @Test
    void objectCRUDInNotFoundBucketTest() {
        String notFoundBucket = "notfoundbucket-"+System.currentTimeMillis();
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(notFoundBucket)
                    .key(key)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }

       try{
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
        try{
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
        }catch (Exception e) {
            testFailed(e);
        } finally{
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
        try{
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
        }catch (Exception e) {
            testFailed(e);
        } finally{
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
                .contentType("application/json")
                .expires(dt)
                .customMetadata(customMeta)
                // the following settings do not affect to the existed object
                .ssecAlgorithm(ssecAlgorithm)
                .ssecKey(ssecKey)
                .ssecKeyMD5(ssecKeyMD5)
//                .serverSideEncryption(ssecAlgorithm) incompatible with ssec
                .storageClass(StorageClassType.STORAGE_CLASS_IA)
                .build();
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            PutObjectOutput putRes = getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
                    .content(content)
                    .build());

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput());
            // default content-type set by sdk
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentType(), "binary/octet-stream");
            Assert.assertNull(headRes.getHeadObjectBasicOutput().getCacheControl());
            Assert.assertNull(headRes.getHeadObjectBasicOutput().getContentEncoding());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().size(), 0);
            Assert.assertNull(headRes.getHeadObjectBasicOutput().getContentLanguage());

            // set object meta
            SetObjectMetaInput input = SetObjectMetaInput.builder().options(options).bucket(bucket).key(key).build();
            getHandler().setObjectMeta(input);

            headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentLength(), data.length());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getEtag(), putRes.getEtag());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getVersionID(), putRes.getVersionID());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCacheControl(), "max-age=600");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentDisposition(), "attachment");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentEncoding(), "deflate");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentLanguage(), "en-US");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getContentType(), "application/json");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getExpiresInDate().toString(), dt.toString());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getExpires(), DateConverter.dateToRFC1123String(dt));
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCacheControl(), "max-age=600");
            Assert.assertNotNull(headRes.getHeadObjectBasicOutput().getCustomMetadata());
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().size(), 2);
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("tag"), "111");
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getCustomMetadata().get("spec"), "cd()*#@*+");
            // can not set ssec to the existed object
            Assert.assertNull(headRes.getHeadObjectBasicOutput().getSsecAlgorithm());
            Assert.assertNull(headRes.getHeadObjectBasicOutput().getSsecKeyMD5());
            // can not change storage class to the existed object
            Assert.assertEquals(headRes.getHeadObjectBasicOutput().getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            clearData(key);
        }
    }

    @Test
    void objectACLTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try{
            PutObjectBasicInput basicInput = PutObjectBasicInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(basicInput)
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
                    .build();
            getHandler().putObjectAcl(input);

            GetObjectACLV2Input get = GetObjectACLV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build();
            GetObjectACLV2Output out = getHandler().getObjectAcl(get);
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
        } finally{
            clearData(key);
        }
    }

    @Test
    void deleteMultiObjectsTest() {
        List<String> objectKeys = null;
        try{
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

            objectKeys = generateDataAndGetKeyList(10, internalObjectCrudPrefix);
            delObjs = new ArrayList<>(objectKeys.size());
            for (int i = 0; i < objectKeys.size(); i++) {
                if (i % 2 == 0) {
                    delObjs.add(ObjectTobeDeleted.builder().key(objectKeys.get(i)).versionID("notexistversionid").build());
                } else {
                    delObjs.add(ObjectTobeDeleted.builder().key(objectKeys.get(i)).build());
                }
            }
            input = DeleteMultiObjectsV2Input.builder()
                    .objects(delObjs)
                    .bucket(bucket)
                    .build();
            output = getHandler().deleteMultiObjects(input);
            Assert.assertNotNull(output.getDeleteds());
            Assert.assertNotNull(output.getErrors());
            Assert.assertEquals(output.getDeleteds().size(), delObjs.size() / 2);
            Assert.assertEquals(output.getErrors().size(), delObjs.size() / 2);
            Assert.assertEquals(output.getErrors().get(0).getCode(), Code.NO_SUCH_KEY);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            if (objectKeys != null) {
                for (String key : objectKeys) {
                    getHandler().deleteObject(DeleteObjectInput.builder().bucket(bucket).key(key).build());
                }
            }
        }
    }

    private List<String> generateDataAndGetKeyList(int num, String pre) {
        List<String> keys = new ArrayList<>(num);
        for(int i = 0; i < num; i++) {
            String keyWithIdx = pre + i;
            getHandler().putObject(PutObjectInput.builder()
                    .putObjectBasicInput(PutObjectBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(keyWithIdx)
                            .build())
                    .content(new ByteArrayInputStream(sampleData.getBytes()))
                    .build());
            keys.add(keyWithIdx);
        }
        return keys;
    }

    private String getUniqueObjectKey() {
        return internalObjectCrudPrefix + StringUtils.randomString(10);
    }
    
    private String getContentMD5(String data) {
        String md5 = null;
        try{
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

    private void clearData(String key) {
        getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
    }

    private void testFailed(Exception e) {
        Consts.LOG.error("object test failed, {}", e.toString());
        Assert.fail();
    }
}
