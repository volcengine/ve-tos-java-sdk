package com.volcengine.tos;

import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.comm.common.TierType;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.acl.*;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.session.Session;
import com.volcengine.tos.session.SessionOptions;
import com.volcengine.tos.transport.TransportConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

public class TOSV2ClientTest {
    private static TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
            .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());

    @Test
    void ClientV1PreSignedURLTest(){
        try{
            TOSClient client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
                    ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)));
            String signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "test.txt", Duration.ofHours(1));
            Consts.LOG.info("presigned url {}", signed);

            client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
                    ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)));
            signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "test.txt", Duration.ofHours(1));
            Consts.LOG.info("presigned url withPathAccessMode {}", signed);

            signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "/test.txt", Duration.ofHours(1));
            Consts.LOG.info("presigned url withPathAccessMode {}", signed);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }
    }

    @Test
    void InitClientBySessionTest() throws TosException {
        TransportConfig config = new TransportConfig().defaultTransportConfig().setReadTimeout(60);
        Session sessionWithConfig = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey),
                SessionOptions.withTransportConfig(config));
        TOSClient client1 = new TOSClient(sessionWithConfig);
        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());

        Session session2 = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey));
        // same transportConfig as client1
        TOSClient client2 = new TOSClient(session2);
        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());
        Assert.assertEquals(60, client2.getConfig().getTransportConfig().getReadTimeout());

        config = new TransportConfig().defaultTransportConfig().setReadTimeout(30);
        Session session3 = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey),
                SessionOptions.withTransportConfig(config));
        // same transportConfig as client1, withTransportConfig does not work
        // so set the transport config at the first init
        TOSClient client3 = new TOSClient(session3);
        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());
        Assert.assertEquals(60, client3.getConfig().getTransportConfig().getReadTimeout());

        // new client3 with new config
        config = new TransportConfig().defaultTransportConfig().setReadTimeout(30);
        TOSClient client4 = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
                ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)),
                ClientOptions.withTransportConfig(config));
        Assert.assertEquals(30, client4.getConfig().getTransportConfig().getReadTimeout());
    }

    @Test
    void ClientNotFoundBucketTest() {
        String notFoundBucket = "notfoundbucket";
        try{
            client.getObject(notFoundBucket, "a.txt");
        } catch (TosException e){
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
        try{
            String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            client.putObject(notFoundBucket, "a.txt", stream);
        } catch (TosException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void ClientListBucketsTest(){
        try{
            ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
            Assert.assertNotNull(output);
            Consts.LOG.info("list {} buckets.", output.getBuckets().length);
            for (int i = 0; i < output.getBuckets().length; i++) {
                Consts.LOG.info("No.{} bucket: {}", i, output.getBuckets()[i].getName());
            }
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }
    }

    @Test
    void ClientNotFoundBucketCURDTest() {
        TOSV2 clientCrudTest = null;
        try{
            TOSClientConfiguration conf = TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
                    .credentials(new StaticCredentials("0000", "ssss")).build();
            clientCrudTest = new TOSV2ClientBuilder().build(conf);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }

        String bucketName = "bucket-curd-test";

        try {
            clientCrudTest.createBucket(new CreateBucketInput(bucketName));
        } catch (TosException e){
            Assert.assertEquals(e.getCode(), Code.INVALID_ACCESS_KEY_ID);
            Assert.assertEquals(e.getStatusCode(), HttpStatus.FORBIDDEN);
        }

        try {
            clientCrudTest.headBucket(bucketName);
        }catch (TosException e) {
            Assert.assertEquals(e.getCode(), Code.NOT_FOUND);
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        try {
            clientCrudTest.deleteBucket(bucketName);
        } catch (TosException e) {
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void ClientBucketCRUDTest() throws InterruptedException {
        String bucketName = "bucket-curd-test"+System.currentTimeMillis();
        try{
            try{
                client.deleteBucket(bucketName);
            } catch (TosException e) {
                // ignore
            }
            try{
                CreateBucketOutput createdBucket = client.createBucket(new CreateBucketInput(bucketName));
                Assert.assertNotNull(createdBucket);
                Consts.LOG.info("bucket created: {}", createdBucket);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
            Thread.sleep(5 * 1000);

            try {
                client.createBucket(new CreateBucketInput(bucketName));
            } catch (TosException e){
                Assert.assertEquals(e.getCode(), Code.BUCKET_ALREADY_OWNED_BY_YOU);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
            }

            int bucketsNum = 0;
            try{
                ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
                Assert.assertNotNull(output);
                Consts.LOG.info("list {} buckets.", output.getBuckets().length);
                bucketsNum = output.getBuckets().length;
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
            try {
                client.deleteBucket(bucketName);
            } catch (TosException e) {
                Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
            Thread.sleep(5 * 1000);
            try {
                client.deleteBucket(bucketName);
            } catch (TosException e) {
                Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
            try{
                ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
                Assert.assertNotNull(output);
                Consts.LOG.info("list {} buckets.", output.getBuckets().length);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }finally {
            try {
                client.deleteBucket(bucketName);
            } catch (TosException e) {
                Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void BucketCopyObjectTest(){
        String srcData = StringUtils.randomString(1024);
        String key = "object-copy-"+System.currentTimeMillis();
        String cpKey = "object-copy-data-"+System.currentTimeMillis();

        try{
            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());
            CopyObjectOutput got = client.copyObject(Consts.bucket, key, cpKey);
            assertDataSame(client, Consts.bucket, cpKey, srcData, got.getEtag());
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
                client.deleteObject(Consts.bucket, cpKey);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void BucketCopyObjectToTest(){
        String srcData = StringUtils.randomString(1024);
        String dstData = StringUtils.randomString(2048);
        String key = "object-copy-to-"+System.currentTimeMillis();
        String cpKey = "object-copy-to-data-"+System.currentTimeMillis();
        try{
            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());

            PutObjectOutput dstPut = client.putObject(Consts.bucketCopy, cpKey, new ByteArrayInputStream(dstData.getBytes()));
            assertDataSame(client, Consts.bucketCopy, cpKey, dstData, dstPut.getEtag());

            CopyObjectOutput gotSrc = client.copyObjectTo(Consts.bucket, Consts.bucketCopy, cpKey, key);
            assertDataSame(client, Consts.bucketCopy, cpKey, srcData, gotSrc.getEtag());
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
                client.deleteObject(Consts.bucketCopy, cpKey);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void BucketCopyObjectFrom(){
        // special case

        String srcData = StringUtils.randomString(1024);
        String dstData = StringUtils.randomString(2048);
        String key = "object-copy-from-"+System.currentTimeMillis();
        String cpKey = "object-copy-from-data-"+System.currentTimeMillis();
        try{
            PutObjectOutput srcPut = client.putObject(Consts.bucketCopy, cpKey, new ByteArrayInputStream(srcData.getBytes()));
            assertDataSame(client, Consts.bucketCopy, cpKey, srcData, srcPut.getEtag());

            PutObjectOutput dstPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(dstData.getBytes()));
            assertDataSame(client, Consts.bucket, key, dstData, dstPut.getEtag());

            CopyObjectOutput got = client.copyObjectFrom(Consts.bucket, Consts.bucketCopy, cpKey, key);
            assertDataSame(client, Consts.bucket, key, srcData, got.getEtag());
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            Assert.assertNotNull(client);
            Assert.assertNotNull(client);
            try{
                client.deleteObject(Consts.bucketCopy, cpKey);
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void copyRangeTest(){
        String cr = TOSClient.copyRange(0, 0);
        Assert.assertEquals("", cr);
        long partSize = 1024L;
        cr = TOSClient.copyRange(0, partSize);
        Assert.assertEquals("bytes=0-1023", cr);
        long startOffset = 128L;
        cr = TOSClient.copyRange(startOffset, partSize);
        Assert.assertEquals("bytes=128-1151", cr);
        cr = TOSClient.copyRange(startOffset, 0);
        Assert.assertEquals("bytes=128-", cr);
    }

    @Test
    void BucketUploadPartCopyTest(){
        String srcData = StringUtils.randomString(20*1024*1024+888);
        String key = "object-upload-part-copy-"+System.currentTimeMillis();
        String dstKey = "objectUploadPartCopy.data";
        try{
            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());

            CreateMultipartUploadOutput upload = client.createMultipartUpload(Consts.bucketCopy, dstKey);

            HeadObjectOutput head = client.headObject(Consts.bucket, key);
            long size = head.getObjectMeta().getContentLength();
            int partSize = 5 * 1024 * 1024; // 5MB
            int partCount = (int) (size / partSize);

            UploadPartCopyOutput[] copyParts = new UploadPartCopyOutput[partCount];
            for (int i = 0; i < partCount; i++) {
                long partLen = partSize;
                if (partCount == i + 1 && (size % partLen) > 0) {
                    partLen += size % (long) partSize;
                }
                long startOffset = (long) i * partSize;

                UploadPartCopyInput input = new UploadPartCopyInput().setUploadID(upload.getUploadID())
                        .setDestinationKey(dstKey).setSourceBucket(Consts.bucket).setStartOffset(startOffset)
                        .setPartSize(partLen).setPartNumber(i+1).setSourceKey(key);
                copyParts[i] = client.uploadPartCopy(Consts.bucketCopy, input);
            }

            MultipartUploadedPart[] part = new MultipartUploadedPart[copyParts.length];
            System.arraycopy(copyParts, 0, part, 0, part.length);

            client.completeMultipartUpload(Consts.bucketCopy, new CompleteMultipartUploadInput(dstKey, upload.getUploadID(), part));

            try(GetObjectOutput got = client.getObject(Consts.bucketCopy, dstKey)){
                Assert.assertEquals(srcData.length(), got.getObjectMeta().getContentLength());
                Assert.assertEquals(srcData, StringUtils.toString(got.getContent(), "content"));
            }
        } catch (TosException | IOException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
                client.deleteObject(Consts.bucketCopy, dstKey);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    private void assertDataSame(TOSV2 client, String bucket, String object, String data, String etag){
        try{
            HeadObjectOutput head = client.headObject(bucket, object);
            Assert.assertEquals(etag, head.getObjectMeta().getEtags());
            Assert.assertEquals(data.length(), head.getObjectMeta().getContentLength());

            GetObjectOutput got = client.getObject(bucket, object);
            String content = StringUtils.toString(got.getContent(), "content");
            Assert.assertEquals(data, content);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }
    }

    @Test
    void MultipartTest(){
        String key = "multipart-test-"+System.currentTimeMillis();
        byte[] data = StringUtils.randomString(5 << 20).getBytes(StandardCharsets.UTF_8);
        CreateMultipartUploadOutput upload = null;
        try{
            upload = client.createMultipartUpload(Consts.bucket, key);
            UploadPartInput input1 = new UploadPartInput(key, upload.getUploadID(), data.length, 1,
                    new TosObjectInputStream(new ByteArrayInputStream(data)));
            UploadPartOutput part1 = client.uploadPart(Consts.bucket, input1);
            UploadPartInput input2 = new UploadPartInput(key, upload.getUploadID(), data.length, 2,
                    new TosObjectInputStream(new ByteArrayInputStream(data)));
            UploadPartOutput part2 = client.uploadPart(Consts.bucket, input2);
            ListUploadedPartsOutput parts = client.listUploadedParts(Consts.bucket, new ListUploadedPartsInput()
                    .setKey(key).setUploadID(upload.getUploadID()));
            Assert.assertEquals(2, parts.getUploadedParts().length);
            CompleteMultipartUploadInput input = new CompleteMultipartUploadInput(key, upload.getUploadID(),
                    new MultipartUploadedPart[]{part1, part2});
            client.completeMultipartUpload(Consts.bucket, input);
            client.deleteObject(Consts.bucket, key);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }
        Assert.assertNotNull(upload);
        try{
            client.abortMultipartUpload(Consts.bucket, new AbortMultipartUploadInput(key, upload.getRequestInfo().getRequestId()));
        } catch (TosException e){
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_UPLOAD);
        }
    }

    @Test
    void MultipartAbortTest(){
        String key = "multipart-test-"+System.currentTimeMillis();
        AbortMultipartUploadInput abort = null;
        try {
            CreateMultipartUploadOutput upload = client.createMultipartUpload(Consts.bucket, key);
            abort = new AbortMultipartUploadInput(key, upload.getUploadID());
            client.abortMultipartUpload(Consts.bucket, abort);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }
        Assert.assertNotNull(abort);
        try{
            client.abortMultipartUpload(Consts.bucket, abort);
        } catch (TosException e){
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_UPLOAD);
        }
    }

    @Test
    void ObjectCURDTest(){
        testObjectCrud(client, Consts.bucket);
    }

    static void testObjectCrud(TOSV2 clientCRUD, String bucket){
        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        String key = "object-curd-"+System.currentTimeMillis();
        try{
            PutObjectOutput put = clientCRUD.putObject(bucket, key, stream);
            GetObjectOutput got = clientCRUD.getObject(bucket, key);
            // NOTICE: 注意在对象很大的时候不要这样一次性读取
            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());

            HeadObjectOutput head = clientCRUD.headObject(bucket, key);
            Assert.assertNotNull(head.getObjectMeta().getEtags());
            Assert.assertEquals(head.getObjectMeta().getEtags(), got.getObjectMeta().getEtags());
            Assert.assertEquals(head.getObjectMeta(), got.getObjectMeta());
        } catch (TosException | IOException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                clientCRUD.deleteObject(bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void SetObjectMetaTest(){
        String data = StringUtils.randomString(1024);
        String key = "object-meta-"+System.currentTimeMillis();
        try{
            PutObjectOutput put = client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))
                    , RequestOptions.withContentType("image"));
            GetObjectOutput got = client.getObject(Consts.bucket, key);
            // NOTICE: 注意在对象很大的时候不要这样一次性读取
            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());

            HeadObjectOutput head = client.headObject(Consts.bucket, key);
            Assert.assertEquals("image", head.getObjectMeta().getContentType());

            client.setObjectMeta(Consts.bucket, key, RequestOptions.withContentType("video"));
            head = client.headObject(Consts.bucket, key);
            Assert.assertEquals("video", head.getObjectMeta().getContentType());
            got = client.getObject(Consts.bucket, key);
            Assert.assertEquals(data, StringUtils.toString(got.getContent(), "content"));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
            Assert.assertEquals("video", got.getObjectMeta().getContentType());

            client.setObjectMeta(Consts.bucket, key, RequestOptions.withContentType("image/png"));
            head = client.headObject(Consts.bucket, key);
            Assert.assertEquals("image/png", head.getObjectMeta().getContentType());
            got = client.getObject(Consts.bucket, key);
            Assert.assertEquals(data, StringUtils.toString(got.getContent(), "content"));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
            Assert.assertEquals("image/png", got.getObjectMeta().getContentType());
        } catch (TosException | IOException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void ObjectListTest(){
        String objectPrefix = "%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95-"+System.currentTimeMillis();
        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
        int number = 12;
        boolean hasMore = true;
        String startAfter = "";
        try{
            for (int i = 0; i < number; i++) {
                PutObjectOutput put = client.putObject(Consts.bucket, objectPrefix + i, new ByteArrayInputStream(data.getBytes()));
                GetObjectOutput got = client.getObject(Consts.bucket, objectPrefix + i);
                // NOTICE: 注意在对象很大的时候不要这样一次性读取
                Assert.assertNotNull(put.getEtag());
                Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));
                Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
            }
            for (int i = 0; i < 10 && hasMore; i++) {
                ListObjectsInput input = new ListObjectsInput().setMarker(startAfter).setMaxKeys(1).setPrefix("%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95");
                ListObjectsOutput resp = client.listObjects(Consts.bucket, input);
                hasMore = resp.isTruncated();
                startAfter = resp.getNextMarker();
            }
            // reverse
            for (int i = 0; i < 10 && hasMore; i++) {
                ListObjectsInput input = new ListObjectsInput().setMarker(startAfter).setMaxKeys(1).setReverse(true);
                ListObjectsOutput resp = client.listObjects(Consts.bucket, input);
                hasMore = resp.isTruncated();
                startAfter = resp.getNextMarker();
            }
        } catch (TosException | IOException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }finally {
            try{
                for (int i = 0; i < number; i++) {
                    client.deleteObject(Consts.bucket, objectPrefix+i);
                }
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void ObjectListVersionsTest(){
        String bucketName = Consts.bucket;
        int number = 12;
        String key = "object-list-version-"+System.currentTimeMillis();
        String[] versionID = new String[number];
        try{
            String buf = StringUtils.randomString(1024);
            for (int i = 0; i < number; i++) {
                PutObjectOutput put = client.putObject(bucketName, key, new ByteArrayInputStream(buf.getBytes(StandardCharsets.UTF_8)));
                versionID[i] = put.getVersionID();
            }
            ListObjectVersionsInput input = new ListObjectVersionsInput().setPrefix(key.substring(0, key.length()-1));
            ListObjectVersionsOutput resp = client.listObjectVersions(bucketName, input);
            // 一次列举完
            Assert.assertFalse(resp.isTruncated());
            Assert.assertNotNull(resp.getVersions());
            Assert.assertEquals(resp.getVersions().length, 12);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }finally {
            try{
                for (int i = 0; i < number; i++) {
                    client.deleteObject(bucketName, key, RequestOptions.withVersionID(versionID[i]));
                }
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void AppendObjectTest(){
        String key = "append-object-"+System.currentTimeMillis();
        String data = StringUtils.randomString(128 << 10);
        try{
            AppendObjectOutput result = client.appendObject(Consts.bucketMultiVersionDisabled, key, new ByteArrayInputStream(data.getBytes()), 0);
//            AppendObjectOutput result = client.appendObject(
//                    AppendObjectInput.builder().contentLength(data.length()).bucket(Consts.bucket).key(key)
//                            .content(new ByteArrayInputStream(data.getBytes())).build());
            GetObjectOutput got = client.getObject(Consts.bucketMultiVersionDisabled, key);
            // NOTICE: 注意在对象很大的时候不要这样一次性读取
            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));

            String data2 = StringUtils.randomString(256 << 10);
            client.appendObject(AppendObjectInput.builder().contentLength(data2.length())
                    .bucket(Consts.bucketMultiVersionDisabled).key(key).content(new ByteArrayInputStream(data2.getBytes()))
                    .offset(result.getNextAppendOffset()).preHashCrc64ecma(result.getHashCrc64ecma()).build());
            got = client.getObject(Consts.bucketMultiVersionDisabled, key);
            // NOTICE: 注意在对象很大的时候不要这样一次性读取
            Assert.assertEquals(data.length()+data2.length(), got.getObjectMeta().getContentLength());
            Assert.assertEquals(crc32Check((data+data2).getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));
        } catch (TosException | IOException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }finally {
            try{
                client.deleteObject(Consts.bucketMultiVersionDisabled, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void DeleteMultiObjectsTest(){
        String data = StringUtils.randomString(1024);
        String objectPrefix = "delete-multi-objects-"+System.currentTimeMillis();
        int number = 12;
        ObjectTobeDeleted[] objectTobeDeleteds = new ObjectTobeDeleted[number];
        try{
            for (int i = 0; i < number; i++) {
                PutObjectOutput put = client.putObject(Consts.bucket, objectPrefix+i, new ByteArrayInputStream(data.getBytes()));
                GetObjectOutput got = client.getObject(Consts.bucket, objectPrefix+i);
                Assert.assertEquals(data, StringUtils.toString(got.getContent(), "content"));
                Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
                objectTobeDeleteds[i] = new ObjectTobeDeleted().setKey(objectPrefix+i);
            }
            DeleteMultiObjectsOutput multiGot = client.deleteMultiObjects(Consts.bucket, new DeleteMultiObjectsInput(objectTobeDeleteds, false));
            Assert.assertNotNull(multiGot);
            Assert.assertNull(multiGot.getErrors());
            Assert.assertEquals(number, multiGot.getDeleteds().length);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try {
                for (int i = 0; i < number; i++) {
                    client.deleteObject(Consts.bucket, objectPrefix + i);
                }
            }catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void GetLargeObjectTest() {
        String data = StringUtils.randomString(16 << 20);
        String key = "object-large-"+System.currentTimeMillis();
        try {
            client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()));
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        }

        // getObject不会直接返回对象的数据，为了避免大对象消耗资源，object.getObjectContent
        // 返回一个InputStream, 这里最好使用try-with-resource来保证输入流被关闭。最好只有在对
        // 象比较小的时候才一次性读取全部数据。
        try (GetObjectOutput object = client.getObject(Consts.bucket, key)) {
            if (object != null) {
                int once, total = 0;
                byte[] buffer = new byte[4096];
                InputStream inputStream = object.getContent();
                while ((once = inputStream.read(buffer)) > 0) {
                    total += once;
                }
                Assert.assertEquals(total, object.getObjectMeta().getContentLength());
                Consts.LOG.info("object's size {}, meta {}", total, object.getObjectMeta());
            } else {
                // key不存在返回null
                Consts.LOG.info("key {} not found", key);
            }
        } catch (TosException | IOException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void GetObjectRangeTest(){
        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        String key = "object-curd-"+System.currentTimeMillis();
        try{
            PutObjectOutput put = client.putObject(Consts.bucket, key, stream);
            GetObjectOutput got = client.getObject(Consts.bucket, key, RequestOptions.withRange(0, 7));
            // NOTICE: 注意在对象很大的时候不要这样一次性读取
            Assert.assertEquals(crc32Check(data.substring(0, 8).getBytes()), crc32Check(StringUtils.toByteArray(got.getContent())));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
        } catch (TosException | IOException e) {
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            try{
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    static int crc32Check(byte[] data){
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(data);
        return (int) crc.getValue();
    }

    @Test
    void ObjectAclTest(){
        String key = "object-acl-"+System.currentTimeMillis();
        try{

            String data = StringUtils.randomString(1024);
            PutObjectOutput put = client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()));
            GetObjectOutput got = client.getObject(Consts.bucket, key);
            Assert.assertEquals(data, StringUtils.toString(got.getContent(), "content"));
            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());

            GetObjectAclOutput gotAcl = client.getObjectAcl(Consts.bucket, key);
            Grant[] grants = new Grant[1];
            grants[0] = new Grant().setPermission(ACLConst.PERMISSION_TYPE_WRITE_ACP)
                    .setGrantee(new Grantee().setId(gotAcl.getGrants()[0].getGrantee().getId())
                            .setType(gotAcl.getGrants()[0].getGrantee().getType()));

            // must set owner, add checker
            PutObjectAclInput input = new PutObjectAclInput().setKey(key)
                    .setAclRules(new ObjectAclRules().setGrants(grants).setOwner(new Owner()));
//            System.out.println(Consts.JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(input));
            client.putObjectAcl(Consts.bucket, input);

            gotAcl = client.getObjectAcl(Consts.bucket, key);
            Assert.assertEquals(1, gotAcl.getGrants().length);
            Assert.assertEquals(ACLConst.PERMISSION_TYPE_WRITE_ACP, gotAcl.getGrants()[0].getPermission());
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            Assert.assertNotNull(client);
            try{
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void PutObjectPrivateACLTest(){
        String key = "object-acl-"+System.currentTimeMillis();
        try {
            String data = StringUtils.randomString(1024);
            client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()));
            PutObjectAclInput input = new PutObjectAclInput().setKey(key)
                    .setAclGrant(new ObjectAclGrant().setAcl(ACLConst.ACL_PRIVATE));
            client.putObjectAcl(Consts.bucket, input);

            GetObjectAclOutput gotAcl = client.getObjectAcl(Consts.bucket, key);
            Assert.assertEquals(1, gotAcl.getGrants().length);
        } catch (TosException e){
            Consts.LOG.error(e.toString(), e);
            Assert.fail();
        } finally {
            Assert.assertNotNull(client);
            try{
                client.deleteObject(Consts.bucket, key);
            } catch (TosException e){
                Consts.LOG.error(e.toString(), e);
                Assert.fail();
            }
        }
    }

    @Test
    void anonymousTest() {
        TOSV2 cli = new TOSV2ClientBuilder().build(Consts.region, Consts.endpoint, "", "");
        try {
            ListBucketsV2Output output = cli.listBuckets(new ListBucketsV2Input());
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertEquals(ex.getStatusCode(), 403);
        }

        PreSignedURLOutput output = cli.preSignedURL(PreSignedURLInput.builder().bucket(Consts.bucket).httpMethod("GET").build());
        Assert.assertTrue(!output.getSignedUrl().contains("X-Tos-Signature"));

        output = client.preSignedURL(PreSignedURLInput.builder().bucket(Consts.bucket).httpMethod("GET").build());
        Assert.assertTrue(output.getSignedUrl().contains("X-Tos-Signature"));
    }

    @Test
    void restoreInfoTest() {
        String key = "restore-info-" + System.currentTimeMillis();
        String data = StringUtils.randomString(1024);
        PutObjectInput input = new PutObjectInput();
        input.setBucket(Consts.bucket);
        input.setKey(key);
        input.setContent(new ByteArrayInputStream(data.getBytes()));
        input.setOptions(new ObjectMetaRequestOptions().setStorageClass(StorageClassType.STORAGE_CLASS_ARCHIVE));
        PutObjectOutput output = client.putObject(input);
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);

        try {
            client.getObject(new GetObjectV2Input().setBucket(Consts.bucket).setKey(key));
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertEquals(ex.getStatusCode(), 403);
            Assert.assertEquals(ex.getCode(), "InvalidObjectState");
            Assert.assertTrue(StringUtils.isNotEmpty(ex.getEc()));
            Assert.assertTrue(StringUtils.isNotEmpty(ex.getRequestUrl()));
            System.out.println(ex.getRequestUrl());
        }

        HeadObjectV2Output houtput = client.headObject(HeadObjectV2Input.builder().bucket(Consts.bucket).key(key).build());
        Assert.assertEquals(houtput.getStorageClass(), StorageClassType.STORAGE_CLASS_ARCHIVE);
        Assert.assertNull(houtput.getRestoreInfo());

        RestoreObjectOutput routput = client.restoreObject(RestoreObjectInput.builder().bucket(Consts.bucket).key(key)
                .days(1).restoreJobParameters(RestoreJobParameters.builder().tier(TierType.TIER_STANDARD).build()).build());
        Assert.assertTrue(routput.getRequestInfo().getRequestId().length() > 0);

        houtput = client.headObject(HeadObjectV2Input.builder().bucket(Consts.bucket).key(key).build());
        Assert.assertEquals(houtput.getStorageClass(), StorageClassType.STORAGE_CLASS_ARCHIVE);
        Assert.assertNotNull(houtput.getRestoreInfo());
        Assert.assertTrue(houtput.getRestoreInfo().getRestoreStatus().isOngoingRequest());
    }

    @Test
    void bucketEncryptionTest() {
        String ep = Consts.endpoint.toLowerCase();
        if (ep.startsWith("http://")) {
            ep = "https://" + ep.substring("http://".length());
        } else if (!ep.startsWith("https://")) {
            ep = "https://" + ep;
        }
        TOSV2 cli = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(ep)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());
        DeleteBucketEncryptionOutput doutput = cli.deleteBucketEncryption(new DeleteBucketEncryptionInput().setBucket(Consts.bucket));
        Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);

        BucketEncryptionRule rule = new BucketEncryptionRule();
        rule.setApplyServerSideEncryptionByDefault(new BucketEncryptionRule.ApplyServerSideEncryptionByDefault().setSseAlgorithm("AES256"));
        PutBucketEncryptionOutput poutput = cli.putBucketEncryption(new PutBucketEncryptionInput().setBucket(Consts.bucket).setRule(rule));
        Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

        GetBucketEncryptionOutput goutput = cli.getBucketEncryption(new GetBucketEncryptionInput().setBucket(Consts.bucket));
        Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
        Assert.assertEquals(goutput.getRule().getApplyServerSideEncryptionByDefault().getSseAlgorithm(), "AES256");

        doutput = cli.deleteBucketEncryption(new DeleteBucketEncryptionInput().setBucket(Consts.bucket));
        Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);
        try {
            cli.getBucketEncryption(new GetBucketEncryptionInput().setBucket(Consts.bucket));
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertEquals(ex.getStatusCode(), 404);
            Assert.assertTrue(ex.getEc().length() > 0);
        }
    }

    @Test
    void bucketTaggingTest() {
        DeleteBucketTaggingOutput doutput = client.deleteBucketTagging(new DeleteBucketTaggingInput().setBucket(Consts.bucket));
        Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);

        TagSet ts = new TagSet();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag().setKey("key1").setValue("value1"));
        tags.add(new Tag().setKey("中文键").setValue("中文值"));
        ts.setTags(tags);
        PutBucketTaggingOutput poutput = client.putBucketTagging(new PutBucketTaggingInput().setBucket(Consts.bucket).setTagSet(ts));
        Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

        GetBucketTaggingOutput goutput = client.getBucketTagging(new GetBucketTaggingInput().setBucket(Consts.bucket));
        Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
        Assert.assertEquals(goutput.getTagSet().getTags().size(), 2);
        Assert.assertEquals(goutput.getTagSet().getTags().get(0).getKey(), "key1");
        Assert.assertEquals(goutput.getTagSet().getTags().get(0).getValue(), "value1");
        Assert.assertEquals(goutput.getTagSet().getTags().get(1).getKey(), "中文键");
        Assert.assertEquals(goutput.getTagSet().getTags().get(1).getValue(), "中文值");

        doutput = client.deleteBucketTagging(new DeleteBucketTaggingInput().setBucket(Consts.bucket));
        Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);
        try {
            client.getBucketTagging(new GetBucketTaggingInput().setBucket(Consts.bucket));
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertEquals(ex.getStatusCode(), 404);
            Assert.assertTrue(ex.getEc().length() > 0);
        }
    }

    @Test
    void expect100ContinueTest() {
        String key = "expect-100-continue-" + System.currentTimeMillis();
        String contentSha256 = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";
        PutObjectOutput output = client.putObject(new PutObjectInput().setOptions(new ObjectMetaRequestOptions().setContentSHA256(contentSha256))
                .setBucket(Consts.bucket).setKey(key).setContent(new ByteArrayInputStream("hello world".getBytes())));
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);


        try (GetObjectV2Output goutput = client.getObject(new GetObjectV2Input().setBucket(Consts.bucket).setKey(key))) {
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            StringUtils.toString(goutput.getContent(), "helloworld");
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        TOSV2 cli = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).
                transportConfig(new TransportConfig().setExcept100ContinueThreshold(1)).build());

        output = cli.putObject(new PutObjectInput().setBucket(Consts.bucket).setKey(key).setContent(new ByteArrayInputStream("hiworld".getBytes())));
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);

        try (GetObjectV2Output goutput = cli.getObject(new GetObjectV2Input().setBucket(Consts.bucket).setKey(key))) {
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            StringUtils.toString(goutput.getContent(), "hiworld");
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }
    }

    @Test
    void disableEncodingMetaTest() {
        String key = "disable-encoding-meta-" + System.currentTimeMillis();
        PutObjectInput input = new PutObjectInput().setBucket(Consts.bucket).setKey(key).setContent(new ByteArrayInputStream("helloworld".getBytes()));
        Map<String, String> meta = new HashMap<>();
        meta.put("key1", "value1");
        meta.put("中文键", "中文值");
        String contentDisposition = "attachment; filename=\"中文.pdf\"";
        input.setOptions(new ObjectMetaRequestOptions().setCustomMetadata(meta).setContentDisposition(contentDisposition));
        PutObjectOutput output = client.putObject(input);
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);

        try (GetObjectV2Output goutput = client.getObject(new GetObjectV2Input().setBucket(Consts.bucket).setKey(key))) {
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(goutput.getCustomMetadata().size(), 2);
            Assert.assertEquals(goutput.getCustomMetadata().get("key1"), "value1");
            Assert.assertEquals(goutput.getCustomMetadata().get("中文键"), "中文值");
            Assert.assertEquals(goutput.getContentDisposition(), contentDisposition);
            StringUtils.toString(goutput.getContent(), "helloworld");
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        TOSV2 cli = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).
                transportConfig(new TransportConfig()).disableEncodingMeta(true).build());

        try {
            cli.putObject(input);
            Assert.assertTrue(false);
        } catch (TosException ex) {
            Assert.assertTrue(ex.getMessage().length() > 0);
            System.out.println(ex.getCause());
        }

        Map<String, String> meta2 = new HashMap<>();
        meta2.put("key1", "value1");
        meta2.put(TosUtils.encodeHeader("中文键"), TosUtils.encodeHeader("中文值"));
        String contentDisposition2 = "attachment; filename=\"" + TosUtils.encodeHeader("中文") + ".pdf\"";
        input.setOptions(new ObjectMetaRequestOptions().setCustomMetadata(meta2).setContentDisposition(contentDisposition2));
        output = cli.putObject(input);
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);

        try (GetObjectV2Output goutput = cli.getObject(new GetObjectV2Input().setBucket(Consts.bucket).setKey(key))) {
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(goutput.getCustomMetadata().size(), 2);
            Assert.assertEquals(goutput.getCustomMetadata().get("key1"), "value1");
            Assert.assertEquals(goutput.getCustomMetadata().get(TosUtils.encodeHeader("中文键").toLowerCase()), TosUtils.encodeHeader("中文值"));
            Assert.assertEquals(goutput.getContentDisposition(), contentDisposition2);
            StringUtils.toString(goutput.getContent(), "helloworld");
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }
    }
}
