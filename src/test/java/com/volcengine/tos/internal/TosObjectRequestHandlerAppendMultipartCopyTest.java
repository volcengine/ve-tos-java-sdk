package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.MetadataDirectiveType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.object.*;
import org.apache.commons.codec.binary.Hex;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.volcengine.tos.Consts.bucket;
import static com.volcengine.tos.Consts.endpoint;


public class TosObjectRequestHandlerAppendMultipartCopyTest {
    private static final String sampleData = StringUtils.randomString(128 << 10);
    private static final String ssecKey = "Y2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2M=";
    private static final String ssecKeyMD5 = "ACdH+Fu9K3HlXdIUBu8GdA==";
    private static final String ssecAlgorithm = "AES256";
    private static final long sampleFileSize = 5 * 1024 * 1024;
    private static final String sampleFilePath = "src/test/resources/uploadPartTest.zip";
    private static String sampleFileMD5 = null;

    private TosObjectRequestHandler getHandler() {
        return ClientInstance.getObjectRequestHandlerInstance();
    }
    private String getUniqueObjectKey() {
        return StringUtils.randomString(10);
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
    void appendObjectTest() {
        String key = Consts.internalObjectAppendPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        try{
            AppendObjectInput input = AppendObjectInput.builder()
                    .contentLength(data.length())
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .content(new ByteArrayInputStream(data.getBytes()))
                    .build();
            AppendObjectOutput appendRes = getHandler().appendObject(input);
            Assert.assertEquals(appendRes.getNextAppendOffset(), data.length());
            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getObjectType(), "Appendable");
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }

            long nextAppendOffset = appendRes.getNextAppendOffset();
            String data2 = sampleData + sampleData + StringUtils.randomString(new Random().nextInt(128));;
            input = AppendObjectInput.builder()
                    .contentLength(data2.length())
                    .offset(nextAppendOffset)
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .content(new ByteArrayInputStream(data2.getBytes()))
                    .build();
            try{
                appendRes = getHandler().appendObject(input);
            } catch (TosException e) {
                Assert.assertNotNull(e);
                Assert.assertEquals(e.getMessage(), "tos: client enable crc64 check but preHashCrc64ecma is not set");
            }
            input.setPreHashCrc64ecma(appendRes.getHashCrc64ecma());
            appendRes = getHandler().appendObject(input);
            Assert.assertEquals(appendRes.getNextAppendOffset(), data2.length() + nextAppendOffset);
            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), data.length() + data2.length());
                Assert.assertEquals(getRes.getObjectType(), "Appendable");
                validateDataSame(data + data2, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e){
            testFailed(e);
        }finally {
            try{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketMultiVersionDisabled).key(key).build());
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void putAndAppendObjectTest(){
        String key = Consts.internalObjectAppendPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        String preHash64 = null;
        try{
            PutObjectOutput output = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .content(new ByteArrayInputStream(data.getBytes()))
                    .build());
            preHash64 = output.getHashCrc64ecma();
        }catch (Exception e) {
            testFailed(e);
        }
        try {
            long nextAppendOffset = data.length();
            String data2 = sampleData + sampleData + StringUtils.randomString(new Random().nextInt(128));;
            AppendObjectInput input = AppendObjectInput.builder()
                    .contentLength(data2.length())
                    .offset(nextAppendOffset)
                    .preHashCrc64ecma(preHash64)
                    .bucket(Consts.bucketMultiVersionDisabled)
                    .key(key)
                    .content(new ByteArrayInputStream(data2.getBytes()))
                    .build();
            getHandler().appendObject(input);
        } catch (TosException e){
            Assert.assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
            Assert.assertEquals(e.getCode(), Code.NOT_APPENDABLE);
        }finally {
            try{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketMultiVersionDisabled).key(key).build());
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void copyObjectInSameBucketTest() {
        String key = Consts.internalObjectCopyPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        PutObjectOutput put = null;
        try{
            put = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(new ByteArrayInputStream(data.getBytes()))
                    .build());
        }catch (Exception e) {
            testFailed(e);
        }
        Assert.assertNotNull(put);
        String copyKey = key + "-copy";
        try{
            // copy object 1 of bucket A to the object 2 of bucket A
            CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    // dst object 2
                    .key(copyKey)
                    .srcBucket(Consts.bucket)
                    // src object 1
                    .srcKey(key)
                    .srcVersionID(put.getVersionID())
                    .build();
            CopyObjectV2Output output1 = getHandler().copyObject(cp1);
            Assert.assertEquals(output1.getEtag(), put.getEtag());
            Assert.assertEquals(output1.getHashCrc64ecma(), put.getHashCrc64ecma());
            Assert.assertEquals(output1.getSourceVersionID(), put.getVersionID());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(copyKey)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), output1.getEtag());
                Assert.assertEquals(getRes.getHashCrc64ecma(), output1.getHashCrc64ecma());
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        }catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(copyKey).build());
        }
    }

    @Test
    void copyObjectInDifferentBucketTest() {
        String key = Consts.internalObjectCopyPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        PutObjectOutput put = null;
        try{
            put = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(new ByteArrayInputStream(data.getBytes()))
                    .build());
        }catch (Exception e) {
            testFailed(e);
        }
        Assert.assertNotNull(put);
        String copyKey = key + "-copy";
        String copyKey1 = copyKey + "--copy";
        try{
            // copy object 1 of bucket A to the object 2 of bucket B
            CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    // dst object 2
                    .key(copyKey)
                    .srcBucket(Consts.bucket)
                    // src object 1
                    .srcKey(key)
                    .srcVersionID(put.getVersionID())
                    .build();
            CopyObjectV2Output output1 = getHandler().copyObject(cp1);
            Assert.assertEquals(output1.getEtag(), put.getEtag());
            Assert.assertEquals(output1.getHashCrc64ecma(), put.getHashCrc64ecma());
            Assert.assertEquals(output1.getSourceVersionID(), put.getVersionID());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(copyKey)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), output1.getEtag());
                Assert.assertEquals(getRes.getHashCrc64ecma(), output1.getHashCrc64ecma());
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }


            // copy object 2 of bucket B to the object 3 of bucket A
            CopyObjectV2Input cp2 = CopyObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    // dst object 3
                    .key(copyKey1)
                    .srcBucket(Consts.bucketCopy)
                    // src object 2
                    .srcKey(copyKey)
                    .build();
            CopyObjectV2Output output2 = getHandler().copyObject(cp2);
            Assert.assertEquals(output2.getEtag(), put.getEtag());
            Assert.assertEquals(output2.getHashCrc64ecma(), put.getHashCrc64ecma());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(copyKey1)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), data.length());
                Assert.assertEquals(getRes.getEtag(), output2.getEtag());
                Assert.assertEquals(getRes.getHashCrc64ecma(), output2.getHashCrc64ecma());
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        }catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(copyKey1).build());
        }
    }

    @Test
    void copyObjectWithReplaceTest() {
        String key = Consts.internalObjectCopyPrefix + getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        PutObjectOutput put = null;
        Map<String, String> cus = new HashMap<>(2);
        cus.put("custom", "volc");
        cus.put("custom1", "volc_test");
        ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
                .customMetadata(cus).build();
        try{
            put = getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(options)
                    .content(new ByteArrayInputStream(data.getBytes()))
                    .build());
        }catch (Exception e) {
            testFailed(e);
        }
        Assert.assertNotNull(put);
        String copyKey = key + "-copy";
        String replaceKey = key + "-replace";
        try{
            // copy object 1 of bucket A to the object 2 of bucket B with default COPY strategy
            CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    // dst object 2
                    .key(copyKey)
                    .srcBucket(Consts.bucket)
                    // src object 1
                    .srcKey(key)
                    .build();
            getHandler().copyObject(cp1);

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(copyKey)
                    .build())){
                Assert.assertNotNull(getRes.getCustomMetadata());
                Assert.assertEquals(getRes.getCustomMetadata().get("custom"), "volc");
                Assert.assertEquals(getRes.getCustomMetadata().get("custom1"), "volc_test");
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }


            // copy object 1 of bucket A to the object 3 of bucket B with REPLACE strategy
            Map<String, String> cusReplace = new HashMap<>(2);
            cusReplace.put("custom", "volc_replace");
            cusReplace.put("custom1", "volc_test_replace");
            ObjectMetaRequestOptions optionsReplace = ObjectMetaRequestOptions.builder()
                    .customMetadata(cusReplace)
                    .build();
            CopyObjectV2Input cp2 = CopyObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    // dst object 3
                    .key(replaceKey)
                    .srcBucket(Consts.bucket)
                    // src object 1
                    .srcKey(key)
                    .metadataDirective(MetadataDirectiveType.METADATA_DIRECTIVE_REPLACE)
                    .options(optionsReplace)
                    .build();
            getHandler().copyObject(cp2);

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(replaceKey)
                    .build())){
                Assert.assertNotNull(getRes.getCustomMetadata());
                Assert.assertEquals(getRes.getCustomMetadata().get("custom"), "volc_replace");
                Assert.assertEquals(getRes.getCustomMetadata().get("custom1"), "volc_test_replace");
                validateDataSame(data, StringUtils.toString(getRes.getContent()));
            } catch (IOException e) {
                testFailed(e);
            }
        }catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(replaceKey).build());
        }
    }

    @Test
    void copyObjectWithIfConditionTest() {
        String key = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        String copyKey = key + "-copy";
        Date lastModified = null;
        String etag = null;
        try{
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(content)
                    .build());
            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .build());
            lastModified = headRes.getLastModifiedInDate();
            etag = headRes.getEtag();
        } catch (Exception e) {
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
            testFailed(e);
        }

        try{
            Assert.assertNotNull(lastModified);
            try {
                CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        // dst object 2
                        .key(copyKey)
                        .srcBucket(Consts.bucket)
                        // src object 1
                        .srcKey(key)
                        .copySourceIfModifiedSince(Date.from(lastModified.toInstant().minusSeconds(120)))
                        .build();
                CopyObjectV2Output output = getHandler().copyObject(cp1);
                Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.OK);
                // check exists
                getHandler().headObject(HeadObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        .key(copyKey)
                        .build());
            } catch (Exception e) {
                testFailed(e);
            } finally{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            }

            try {
                CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        // dst object 2
                        .key(copyKey)
                        .srcBucket(Consts.bucket)
                        // src object 1
                        .srcKey(key)
                        .copySourceIfUnmodifiedSince(lastModified)
                        .build();
                CopyObjectV2Output output = getHandler().copyObject(cp1);
                Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.OK);
                // check exists
                getHandler().headObject(HeadObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        .key(copyKey)
                        .build());
            } catch (Exception e) {
                testFailed(e);
            } finally{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            }

            Assert.assertNotNull(etag);
            try {
                CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        // dst object 2
                        .key(copyKey)
                        .srcBucket(Consts.bucket)
                        // src object 1
                        .srcKey(key)
                        .copySourceIfMatch(etag)
                        .build();
                CopyObjectV2Output output = getHandler().copyObject(cp1);
                Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.OK);
                getHandler().headObject(HeadObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        .key(copyKey)
                        .build());
            } catch (Exception e) {
                testFailed(e);
            } finally{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            }

            try {
                CopyObjectV2Input cp1 = CopyObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        // dst object 2
                        .key(copyKey)
                        .srcBucket(Consts.bucket)
                        // src object 1
                        .srcKey(key)
                        .copySourceIfNoneMatch(etag + "xxx")
                        .build();
                CopyObjectV2Output output = getHandler().copyObject(cp1);
                Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.OK);
                getHandler().headObject(HeadObjectV2Input.builder()
                        .bucket(Consts.bucketCopy)
                        .key(copyKey)
                        .build());
            } catch (Exception e) {
                testFailed(e);
            } finally{
                getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(copyKey).build());
            }
        }catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
        }
    }

    /*
        multi part tests
     */
    @Test
    void createMultipartUploadTest() {
        String uniqueKey = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        String uploadID = null;
        try{
            CreateMultipartUploadInput input = CreateMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build();
            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(input);
            Assert.assertEquals(output.getBucket(), Consts.bucket);
            Assert.assertEquals(output.getKey(), uniqueKey);
            Assert.assertNotNull(output.getUploadID());
            uploadID = output.getUploadID();
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .build());
        }
    }

    @Test
    void completeMultipartUploadTest() {
        String uniqueKey = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        String uploadID = null;
        try{
            CreateMultipartUploadInput input = CreateMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build();
            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(input);
            uploadID = output.getUploadID();

            UploadPartV2Output uploadPartV2Output = getHandler().uploadPart(UploadPartV2Input.builder()
                    .uploadPartBasicInput(UploadPartBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(uniqueKey)
                            .uploadID(uploadID)
                            .partNumber(1)
                            .build())
                    .contentLength(getPartSize())
                    .content(getPartData())
                    .build());
            List<UploadedPartV2> uploadedPartV2List = new ArrayList<>(1);
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(1).etag(uploadPartV2Output.getEtag()).build());
            CompleteMultipartUploadV2Input complete = CompleteMultipartUploadV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .uploadedParts(uploadedPartV2List)
                    .build();
            CompleteMultipartUploadV2Output completeOutput = getHandler().completeMultipartUpload(complete);
            Assert.assertEquals(completeOutput.getBucket(), Consts.bucket);
            Assert.assertEquals(completeOutput.getKey(), uniqueKey);
            Assert.assertNotNull(completeOutput.getLocation());
            Assert.assertNotNull(completeOutput.getEtag());

            HeadObjectV2Output head = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build());
            Assert.assertEquals(head.getContentLength(), getPartSize());
        } catch (Exception e) {
            testFailed(e);
        } finally{
            try{
                getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                        .bucket(Consts.bucket)
                        .key(uniqueKey)
                        .uploadID(uploadID)
                        .build());
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Test
    void uploadPartTest() {
        String uniqueKey = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        String uploadID = null;

        // file split
        InputStream part1 = null;
        InputStream part2 = null;
        InputStream part3 = null;
        long fileLength = -1;
        try{
            FileInputStream fis1 = new FileInputStream(sampleFilePath);
            part1 = new TosRepeatableBoundedFileInputStream(fis1, getPartSize());

            FileInputStream fis2 = new FileInputStream(sampleFilePath);
            fis2.skip(getPartSize());
            part2 = new TosRepeatableBoundedFileInputStream(fis2, getPartSize());

            File f = new File(sampleFilePath);
            fileLength = f.length();
            FileInputStream fis3 = new FileInputStream(sampleFilePath);
            fis3.skip(getPartSize() + getPartSize());
            part3 = new TosRepeatableBoundedFileInputStream(fis3, fileLength - getPartSize() * 2);
        } catch (IOException e) {
            testFailed(e);
        }
        Assert.assertNotNull(part1);
        Assert.assertNotNull(part2);
        Assert.assertNotNull(part3);
        try{
            CreateMultipartUploadInput input = CreateMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build();
            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(input);
            uploadID = output.getUploadID();

            List<UploadedPartV2> uploadedPartV2List = new ArrayList<>(3);

            UploadPartV2Output uploadPartV2Output1 = getHandler().uploadPart(UploadPartV2Input.builder()
                    .uploadPartBasicInput(UploadPartBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(uniqueKey)
                            .uploadID(uploadID)
                            .partNumber(1)
                            .build())
                    .contentLength(getPartSize())
                    .content(part1)
                    .build());
            Assert.assertEquals(uploadPartV2Output1.getPartNumber(), 1);
            Assert.assertNotNull(uploadPartV2Output1.getEtag());
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(1).etag(uploadPartV2Output1.getEtag()).build());

            UploadPartV2Output uploadPartV2Output2 = getHandler().uploadPart(UploadPartV2Input.builder()
                    .uploadPartBasicInput(UploadPartBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(uniqueKey)
                            .uploadID(uploadID)
                            .partNumber(2)
                            .build())
                    .contentLength(getPartSize())
                    .content(part2)
                    .build());
            Assert.assertEquals(uploadPartV2Output2.getPartNumber(), 2);
            Assert.assertNotNull(uploadPartV2Output2.getEtag());
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(2).etag(uploadPartV2Output2.getEtag()).build());

            UploadPartV2Output uploadPartV2Output3 = getHandler().uploadPart(UploadPartV2Input.builder()
                    .uploadPartBasicInput(UploadPartBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(uniqueKey)
                            .uploadID(uploadID)
                            .partNumber(3)
                            .build())
                    .contentLength(fileLength - getPartSize() - getPartSize())
                    .content(part3)
                    .build());
            Assert.assertEquals(uploadPartV2Output3.getPartNumber(), 3);
            Assert.assertNotNull(uploadPartV2Output3.getEtag());
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(3).etag(uploadPartV2Output3.getEtag()).build());

            CompleteMultipartUploadV2Input complete = CompleteMultipartUploadV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .uploadedParts(uploadedPartV2List)
                    .build();
            CompleteMultipartUploadV2Output completeOutput = getHandler().completeMultipartUpload(complete);
            Assert.assertEquals(completeOutput.getBucket(), Consts.bucket);
            Assert.assertEquals(completeOutput.getKey(), uniqueKey);
            Assert.assertNotNull(completeOutput.getLocation());
            Assert.assertNotNull(completeOutput.getEtag());

            try(GetObjectV2Output getRes = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build())){
                Assert.assertEquals(getRes.getContentLength(), fileLength);
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = getRes.getContent().read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            try{
                getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                        .bucket(Consts.bucket)
                        .key(uniqueKey)
                        .uploadID(uploadID)
                        .build());
            } catch (Exception e) {
                // ignore
            }
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(uniqueKey).build());
        }
    }

    @Test
    void multipartWithMetaInfoTest() {
        Date dt = Date.from(LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.UTC));
        Map<String, String> meta = new HashMap<>(2);
        meta.put("custom1", "volc_part");
        meta.put("custom2", "volc_part1");
        ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
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
                .grantFullControl("id=123")
                .grantWriteAcp("id=234")
                .grantWrite("id=345")
                .grantReadAcp("id=456")
                .grantRead("id=567")
                .customMetadata(meta)
                .build();
        String uniqueKey = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        String uploadID = null;
        try{
            CreateMultipartUploadInput input = CreateMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .options(options)
                    .build();
            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(input);
            uploadID = output.getUploadID();

            UploadPartV2Output uploadPartV2Output = getHandler().uploadPart(UploadPartV2Input.builder()
                    .uploadPartBasicInput(UploadPartBasicInput.builder()
                            .bucket(Consts.bucket)
                            .key(uniqueKey)
                            .uploadID(uploadID)
                            .partNumber(1)
                            .options(ObjectMetaRequestOptions.builder()
                                    // use ssec, so we need to set header options to head/get an object
                                    .ssecAlgorithm(ssecAlgorithm)
                                    .ssecKey(ssecKey)
                                    .ssecKeyMD5(ssecKeyMD5)
                                    .build())
                            .build())
                    .contentLength(getPartSize())
                    .content(getPartData())
                    .build());
            List<UploadedPartV2> uploadedPartV2List = new ArrayList<>(1);
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(1).etag(uploadPartV2Output.getEtag()).build());
            CompleteMultipartUploadV2Input complete = CompleteMultipartUploadV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .uploadedParts(uploadedPartV2List)
                    .build();
            getHandler().completeMultipartUpload(complete);

            HeadObjectV2Output headRes = getHandler().headObject(HeadObjectV2Input.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .options(ObjectMetaRequestOptions.builder()
                            // use ssec, so we need to set header options to head/get an object
                            .ssecAlgorithm(ssecAlgorithm)
                            .ssecKey(ssecKey)
                            .ssecKeyMD5(ssecKeyMD5)
                            .build())
                    .build());
            Assert.assertEquals(headRes.getContentLength(), getPartSize());
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
            Assert.assertNotNull(headRes.getCustomMetadata());
            Assert.assertEquals(headRes.getCustomMetadata().size(), 2);
            Assert.assertEquals(headRes.getCustomMetadata().get("custom1"), "volc_part");
            Assert.assertEquals(headRes.getCustomMetadata().get("custom2"), "volc_part1");
        } catch (Exception e) {
            testFailed(e);
        } finally{
            try{
                getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                        .bucket(Consts.bucket)
                        .key(uniqueKey)
                        .uploadID(uploadID)
                        .build());
            } catch (Exception e) {
                // ignore
            }
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(uniqueKey).build());
        }
    }

    @Test
    void listMultipartUploadsTest() {
        String uniqueKeyPrefix = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        List<List<String>> keyAndUploadIDList = new ArrayList<>(10);
        try{
            for(int i = 0; i < 5; i++) {
                CreateMultipartUploadOutput output = getHandler().createMultipartUpload(
                        CreateMultipartUploadInput.builder().bucket(Consts.bucket).key(uniqueKeyPrefix + i).build()
                );
                List<String> keyAndUploadID = new ArrayList<>();
                keyAndUploadID.add(uniqueKeyPrefix + i);
                keyAndUploadID.add(output.getUploadID());
                keyAndUploadIDList.add(keyAndUploadID);
                output = getHandler().createMultipartUpload(
                        CreateMultipartUploadInput.builder().bucket(Consts.bucket).key(uniqueKeyPrefix + i).build()
                );
                keyAndUploadID = new ArrayList<>();
                keyAndUploadID.add(uniqueKeyPrefix + i);
                keyAndUploadID.add(output.getUploadID());
                keyAndUploadIDList.add(keyAndUploadID);
            }
            // list all
            ListMultipartUploadsV2Output output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(1000)
                            .prefix(uniqueKeyPrefix)
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 10);
            Assert.assertEquals(output.getUploads().get(0).getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);


            // list by keyMarker
            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 3);
            Assert.assertTrue(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), uniqueKeyPrefix + 1);

            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .keyMarker(output.getNextKeyMarker())
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 3);
            Assert.assertTrue(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), uniqueKeyPrefix + 3);

            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .keyMarker(output.getNextKeyMarker())
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 2);
            Assert.assertFalse(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), "");


            // list by keyMarker and uploadIDMarker
            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 3);
            Assert.assertTrue(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), uniqueKeyPrefix + 1);

            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .keyMarker(output.getNextKeyMarker())
                            .uploadIDMarker(output.getNextUploadIdMarker())
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 3);
            Assert.assertTrue(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), uniqueKeyPrefix + 2);

            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .keyMarker(output.getNextKeyMarker())
                            .uploadIDMarker(output.getNextUploadIdMarker())
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 3);
            Assert.assertTrue(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), uniqueKeyPrefix + 4);

            output = getHandler().listMultipartUploads(
                    ListMultipartUploadsV2Input.builder()
                            .bucket(Consts.bucket)
                            .maxUploads(3)
                            .prefix(uniqueKeyPrefix)
                            .keyMarker(output.getNextKeyMarker())
                            .uploadIDMarker(output.getNextUploadIdMarker())
                            .build());
            Assert.assertNotNull(output.getUploads());
            Assert.assertEquals(output.getUploads().size(), 1);
            Assert.assertFalse(output.isTruncated());
            Assert.assertEquals(output.getNextKeyMarker(), "");
        } catch (Exception e) {
            testFailed(e);
        } finally{
            // clear data
            try{
                for (List<String> keyAndUploadID : keyAndUploadIDList) {
                    getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                            .bucket(Consts.bucket)
                            .key(keyAndUploadID.get(0))
                            .uploadID(keyAndUploadID.get(1))
                            .build());
                }
            } catch (TosException e) {
                // ignore
            } catch (Exception e) {
                testFailed(e);
            }
        }
    }

    @Test
    void listPartsTest() {
        String uniqueKey = Consts.internalObjectMultiPartPrefix + getUniqueObjectKey();
        String uploadID = null;
        try{
            CreateMultipartUploadInput input = CreateMultipartUploadInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .build();
            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(input);
            uploadID = output.getUploadID();

            for (int i = 1; i <= 10; i++) {
                getHandler().uploadPart(UploadPartV2Input.builder()
                        .uploadPartBasicInput(UploadPartBasicInput.builder()
                                .bucket(Consts.bucket)
                                .key(uniqueKey)
                                .uploadID(uploadID)
                                .partNumber(i)
                                .build())
                        .contentLength(getPartSize())
                        .content(getPartData())
                        .build());
            }
            // list all
            ListPartsInput list1 = ListPartsInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .maxParts(1000)
                    .build();
            ListPartsOutput output1 = getHandler().listParts(list1);
            Assert.assertEquals(output1.getBucket(), Consts.bucket);
            Assert.assertEquals(output1.getKey(), uniqueKey);
            Assert.assertEquals(output1.getMaxParts(), 1000);
            Assert.assertNotNull(output1.getOwner().getId());
            Assert.assertNotNull(output1.getUploadedParts());
            Assert.assertEquals(output1.getUploadedParts().size(), 10);
            Assert.assertFalse(output1.isTruncated());
            for (UploadedPartV2 partV2 : output1.getUploadedParts()) {
                Assert.assertNotNull(partV2.getEtag());
                Assert.assertNotEquals(partV2.getPartNumber(), 0);
                Assert.assertEquals(partV2.getSize(), getPartSize());
            }

            // list by partNumberMarker
            list1 = ListPartsInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .maxParts(4)
                    .build();
            output1 = getHandler().listParts(list1);
            Assert.assertEquals(output1.getBucket(), Consts.bucket);
            Assert.assertEquals(output1.getKey(), uniqueKey);
            Assert.assertEquals(output1.getMaxParts(), 4);
            Assert.assertTrue(output1.isTruncated());
            Assert.assertNotNull(output1.getUploadedParts());
            Assert.assertEquals(output1.getUploadedParts().size(), 4);

            list1 = ListPartsInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .maxParts(4)
                    .partNumberMarker(output1.getNextPartNumberMarker())
                    .build();
            output1 = getHandler().listParts(list1);
            Assert.assertEquals(output1.getBucket(), Consts.bucket);
            Assert.assertEquals(output1.getKey(), uniqueKey);
            Assert.assertEquals(output1.getMaxParts(), 4);
            Assert.assertTrue(output1.isTruncated());
            Assert.assertNotNull(output1.getUploadedParts());
            Assert.assertEquals(output1.getUploadedParts().size(), 4);

            list1 = ListPartsInput.builder()
                    .bucket(Consts.bucket)
                    .key(uniqueKey)
                    .uploadID(uploadID)
                    .maxParts(4)
                    .partNumberMarker(output1.getNextPartNumberMarker())
                    .build();
            output1 = getHandler().listParts(list1);
            Assert.assertEquals(output1.getBucket(), Consts.bucket);
            Assert.assertEquals(output1.getKey(), uniqueKey);
            Assert.assertEquals(output1.getMaxParts(), 4);
            Assert.assertFalse(output1.isTruncated());
            Assert.assertNotNull(output1.getUploadedParts());
            Assert.assertEquals(output1.getUploadedParts().size(), 2);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            try{
                getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                        .bucket(Consts.bucket)
                        .key(uniqueKey)
                        .uploadID(uploadID)
                        .build());
            } catch (TosException e) {
                // ignore
            } catch (Exception e) {
                testFailed(e);
            }
        }
    }

    @Test
    void uploadPartCopyTest() {
        String key = getUniqueObjectKey();
        String dstKey = key + "-copy";
        File file = new File(sampleFilePath);
        String uploadID = null;
        try(FileInputStream fis = new FileInputStream(file)){
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .content(fis)
                    .build());

            CreateMultipartUploadOutput output = getHandler().createMultipartUpload(
                    CreateMultipartUploadInput.builder().bucket(Consts.bucketCopy).key(dstKey).build());
            uploadID = output.getUploadID();
            UploadPartCopyV2Input input = UploadPartCopyV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(dstKey)
                    .sourceBucket(Consts.bucket)
                    .sourceKey(key)
                    .uploadID(uploadID)
                    .partNumber(1)
                    .copySourceRange(0, 5 * 1024 * 1024)
                    .build();
            UploadPartCopyV2Output output1 = getHandler().uploadPartCopy(input);
            Assert.assertEquals(output1.getPartNumber(), 1);
            Assert.assertNotNull(output1.getEtag());

            input = UploadPartCopyV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(dstKey)
                    .sourceBucket(Consts.bucket)
                    .sourceKey(key)
                    .uploadID(uploadID)
                    .partNumber(2)
                    .copySourceRange(5 * 1024 * 1024 + 1, file.length() -1)
                    .build();
            UploadPartCopyV2Output output2 = getHandler().uploadPartCopy(input);
            Assert.assertEquals(output2.getPartNumber(), 2);
            Assert.assertNotNull(output2.getEtag());

            List<UploadedPartV2> uploadedPartV2List = new ArrayList<>(2);
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(1).etag(output1.getEtag()).build());
            uploadedPartV2List.add(UploadedPartV2.builder().partNumber(2).etag(output2.getEtag()).build());
            CompleteMultipartUploadV2Input complete = CompleteMultipartUploadV2Input.builder()
                    .bucket(Consts.bucketCopy)
                    .key(dstKey)
                    .uploadID(uploadID)
                    .uploadedParts(uploadedPartV2List)
                    .build();
            CompleteMultipartUploadV2Output completeOutput = getHandler().completeMultipartUpload(complete);
            Assert.assertEquals(completeOutput.getBucket(), Consts.bucketCopy);
            Assert.assertEquals(completeOutput.getKey(), dstKey);
            Assert.assertNotNull(completeOutput.getLocation());
            Assert.assertNotNull(completeOutput.getEtag());

            try(GetObjectV2Output getSrc = getHandler().getObject(GetObjectV2Input.builder()
                    .bucket(Consts.bucket).key(key).build());
                GetObjectV2Output getDst = getHandler().getObject(GetObjectV2Input.builder()
                        .bucket(Consts.bucketCopy).key(dstKey).build())){
                Assert.assertEquals(getSrc.getContentLength(), file.length());
                Assert.assertEquals(getDst.getContentLength(), file.length());
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = getSrc.getContent().read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                String srcMD5 = new String(Hex.encodeHex(md5.digest()));
                md5 = MessageDigest.getInstance("MD5");
                buffer = new byte[8192];
                length = 0;
                while ((length = getDst.getContent().read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                String dstMD5 = new String(Hex.encodeHex(md5.digest()));
                Assert.assertEquals(srcMD5, dstMD5);
            } catch (IOException e) {
                testFailed(e);
            }
        } catch (Exception e) {
            testFailed(e);
        } finally{
            if (uploadID != null) {
                try{
                    getHandler().abortMultipartUpload(AbortMultipartUploadInput.builder()
                            .bucket(Consts.bucketCopy).key(dstKey).uploadID(uploadID).build());
                } catch (TosException e) {
                    // ignore
                } catch (Exception e) {
                    testFailed(e);
                }
            }
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucket).key(key).build());
            getHandler().deleteObject(DeleteObjectInput.builder().bucket(Consts.bucketCopy).key(dstKey).build());
        }
    }

    @Test
    void fetchObjectTest() {
        String key = getUniqueObjectKey();
        String fetchKey = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(new ObjectMetaRequestOptions().setAclType(ACLType.ACL_PUBLIC_READ))
                    .content(content)
                    .build());
            HeadObjectV2Output head = getHandler().headObject(new HeadObjectV2Input().setBucket(bucket).setKey(key));
            String crc64 = head.getHashCrc64ecma();

            // fetch
            Map<String, String> meta = new HashMap<>();
            meta.put("test-key", "test-value");
            FetchObjectInput input = new FetchObjectInput().setBucket(bucket).setKey(fetchKey)
                    .setOptions(new ObjectMetaRequestOptions().setAclType(ACLType.ACL_PRIVATE)
                            .setStorageClass(StorageClassType.STORAGE_CLASS_IA)
                            .setCustomMetadata(meta))
                    .setUrl("https://" + bucket + "." + endpoint + "/" + key);
            FetchObjectOutput output = getHandler().fetchObject(input);
            Assert.assertNotNull(output);
            Assert.assertNotNull(output.getEtag());

            // head
            head = getHandler().headObject(new HeadObjectV2Input().setBucket(bucket).setKey(fetchKey));
            Assert.assertNotNull(head);
            Assert.assertEquals(head.getEtag(), output.getEtag());
            Assert.assertEquals(head.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
            Assert.assertEquals(head.getContentLength(), data.length());
            Assert.assertNotNull(head.getCustomMetadata());
            Assert.assertEquals(head.getCustomMetadata().get("test-key"), "test-value");
            Assert.assertEquals(head.getHashCrc64ecma(), crc64);
        } catch (TosException e) {
            testFailed(e);
        }
    }

    @Test
    void putFetchTaskTest() {
        String key = getUniqueObjectKey();
        String fetchKey = getUniqueObjectKey();
        String data = sampleData + StringUtils.randomString(new Random().nextInt(128));
        InputStream content = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        try {
            getHandler().putObject(PutObjectInput.builder()
                    .bucket(Consts.bucket)
                    .key(key)
                    .options(new ObjectMetaRequestOptions().setAclType(ACLType.ACL_PUBLIC_READ))
                    .content(content)
                    .build());
            HeadObjectV2Output head = getHandler().headObject(new HeadObjectV2Input().setBucket(bucket).setKey(key));
            String crc64 = head.getHashCrc64ecma();
            String etag = head.getEtag();

            // fetch
            Map<String, String> meta = new HashMap<>();
            meta.put("test-key", "test-value");
            PutFetchTaskInput input = new PutFetchTaskInput().setBucket(bucket).setKey(fetchKey)
                    .setOptions(new ObjectMetaRequestOptions().setAclType(ACLType.ACL_PRIVATE)
                            .setStorageClass(StorageClassType.STORAGE_CLASS_IA)
                            .setCustomMetadata(meta))
                    .setUrl("https://" + bucket + "." + endpoint + "/" + key);
            PutFetchTaskOutput output = getHandler().putFetchTask(input);
            Assert.assertNotNull(output);
            Assert.assertNotNull(output.getTaskID());
//
//            Thread.sleep(5 * 1000);
//
//            // head
//            head = getHandler().headObject(new HeadObjectV2Input().setBucket(bucket).setKey(fetchKey));
//            Assert.assertNotNull(head);
//            Assert.assertEquals(head.getEtag(), etag);
//            Assert.assertEquals(head.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
//            Assert.assertEquals(head.getContentLength(), data.length());
//            Assert.assertNotNull(head.getCustomMetadata());
//            Assert.assertEquals(head.getCustomMetadata().get("test-key"), "test-value");
//            Assert.assertEquals(head.getHashCrc64ecma(), crc64);
        } catch (TosException e) {
            testFailed(e);
        }
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

    private InputStream getPartData() {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(sampleFilePath);
        } catch (IOException e) {
            testFailed(e);
        }
        return new TosRepeatableBoundedFileInputStream(fis, getPartSize());
    }

    private long getPartSize() {
        return sampleFileSize;
    }

    public String getMD5() {
        if (sampleFileMD5 != null) {
            return sampleFileMD5;
        }
        synchronized (this) {
            try(FileInputStream fileInputStream = new FileInputStream(sampleFilePath)) {
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

    private void testFailed(Exception e) {
        Consts.LOG.error("object test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
