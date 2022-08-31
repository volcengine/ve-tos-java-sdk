//package com.volcengine.tos;
//
//import com.volcengine.tos.auth.StaticCredentials;
//import com.volcengine.tos.comm.Code;
//import com.volcengine.tos.comm.HttpMethod;
//import com.volcengine.tos.model.acl.*;
//import com.volcengine.tos.model.acl.Owner;
//import com.volcengine.tos.model.bucket.*;
//import com.volcengine.tos.model.object.*;
//import com.volcengine.tos.session.Session;
//import com.volcengine.tos.session.SessionOptions;
//import com.volcengine.tos.transport.TransportConfig;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.http.HttpStatus;
//import org.testng.Assert;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.zip.CRC32;
//
//public class DemoTest {
//    private static TOSClient client = null;
//
//    @BeforeTest
//    public static void init() throws TosException {
//        client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
//                ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)));
//    }
//
//    @Test
//    void preSignedURLTest(){
//        try{
//            TOSClient client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
//                    ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)));
//            String signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "test.txt", Duration.ofHours(1));
//            Consts.LOG.info("presigned url {}", signed);
//
//            client = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
//                    ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)));
//            signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "test.txt", Duration.ofHours(1));
//            Consts.LOG.info("presigned url withPathAccessMode {}", signed);
//
//            signed = client.preSignedURL(HttpMethod.GET, Consts.bucket, "/test.txt", Duration.ofHours(1));
//            Consts.LOG.info("presigned url withPathAccessMode {}", signed);
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }
//    }
//
//    @Test
//    void initClientBySessionTest() throws TosException {
//        TransportConfig config = new TransportConfig().defaultTransportConfig().setReadTimeout(60);
//        Session sessionWithConfig = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey),
//                SessionOptions.withTransportConfig(config));
//        TOSClient client1 = new TOSClient(sessionWithConfig);
//        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());
//
//        Session session2 = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey));
//        // same transportConfig as client1
//        TOSClient client2 = new TOSClient(session2);
//        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());
//        Assert.assertEquals(60, client2.getConfig().getTransportConfig().getReadTimeout());
//
//        config = new TransportConfig().defaultTransportConfig().setReadTimeout(30);
//        Session session3 = new Session(Consts.endpoint, Consts.region, new StaticCredentials(Consts.accessKey, Consts.secretKey),
//                SessionOptions.withTransportConfig(config));
//        // same transportConfig as client1, withTransportConfig does not work
//        // so set the transport config at the first init
//        TOSClient client3 = new TOSClient(session3);
//        Assert.assertEquals(60, client1.getConfig().getTransportConfig().getReadTimeout());
//        Assert.assertEquals(60, client3.getConfig().getTransportConfig().getReadTimeout());
//
//        // new client3 with new config
//        config = new TransportConfig().defaultTransportConfig().setReadTimeout(30);
//        TOSClient client4 = new TOSClient(Consts.endpoint, ClientOptions.withRegion(Consts.region),
//                ClientOptions.withCredentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)),
//                ClientOptions.withTransportConfig(config));
//        Assert.assertEquals(30, client4.getConfig().getTransportConfig().getReadTimeout());
//    }
//
//    @Test
//    void notFoundBucketTest() {
//        String notFoundBucket = "notfoundbucket";
//        try{
//            client.getObject(notFoundBucket, "a.txt");
//        } catch (TosException e){
//            Assert.assertNotNull(e);
//            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
//            Assert.assertEquals(e.getStatusCode(), HttpStatus.SC_NOT_FOUND);
//        }
//        try{
//            String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
//            InputStream stream = new ByteArrayInputStream(data.getBytes());
//            client.putObject(notFoundBucket, "a.txt", stream);
//        } catch (TosException e) {
//            Assert.assertNotNull(e);
//            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
//            Assert.assertEquals(e.getStatusCode(), HttpStatus.SC_NOT_FOUND);
//        }
//    }
//
//    @Test
//    void bucketCRUDTest() throws InterruptedException {
//        String bucketName = "bucket-curd-test"+System.currentTimeMillis();
//        try{
//            try{
//                client.deleteBucket(bucketName);
//            } catch (TosException e) {
//                // ignore
//            }
//            try{
//                CreateBucketOutput createdBucket = client.createBucket(new CreateBucketInput(bucketName));
//                Assert.assertNotNull(createdBucket);
//                Consts.LOG.info("bucket created: {}", createdBucket);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//
//            try {
//                client.createBucket(new CreateBucketInput(bucketName));
//            } catch (TosException e){
//                Assert.assertEquals(e.getCode(), Code.BUCKET_ALREADY_OWNED_BY_YOU);
//                Assert.assertEquals(e.getStatusCode(), HttpStatus.SC_CONFLICT);
//            }
//
//            Thread.sleep(3 * 1000);
//
//            try {
//                HeadBucketOutput output = client.headBucket(bucketName);
//                Assert.assertNotNull(output);
//                Consts.LOG.info("bucket region {}", output.getRegion());
//            } catch (TosException e) {
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//
//            int bucketsNum = 0;
//            try{
//                ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
//                Assert.assertNotNull(output);
//                Consts.LOG.info("list {} buckets.", output.getBuckets().length);
//                bucketsNum = output.getBuckets().length;
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//            try {
//                client.deleteBucket(bucketName);
//            } catch (TosException e) {
//                Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
//                Assert.assertEquals(e.getStatusCode(), HttpStatus.SC_NOT_FOUND);
//            }
//            try{
//                ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
//                Assert.assertNotNull(output);
//                Consts.LOG.info("list {} buckets.", output.getBuckets().length);
//                Assert.assertEquals(output.getBuckets().length, bucketsNum-1);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }finally {
//            try {
//                client.deleteBucket(bucketName);
//            } catch (TosException e) {
//                Assert.assertEquals(e.getCode(), "NotFound");
//                Assert.assertEquals(e.getStatusCode(), HttpStatus.SC_NOT_FOUND);
//            }
//        }
//    }
//
//    @Test
//    void copyObjectTest(){
//        String srcData = RandomStringUtils.randomAlphanumeric(1024);
//        String key = "object-copy-"+System.currentTimeMillis();
//        String cpKey = "object-copy-data-"+System.currentTimeMillis();
//
//        try{
//            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
//            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());
//            CopyObjectOutput got = client.copyObject(Consts.bucket, key, cpKey);
//            assertDataSame(client, Consts.bucket, cpKey, srcData, got.getEtag());
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//                client.deleteObject(Consts.bucket, cpKey);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void copyObjectToTest(){
//        String srcData = RandomStringUtils.randomAlphanumeric(1024);
//        String dstData = RandomStringUtils.randomAlphanumeric(2048);
//        String key = "object-copy-to-"+System.currentTimeMillis();
//        String cpKey = "object-copy-to-data-"+System.currentTimeMillis();
//        try{
//            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
//            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());
//
//            PutObjectOutput dstPut = client.putObject(Consts.bucketCopy, cpKey, new ByteArrayInputStream(dstData.getBytes()));
//            assertDataSame(client, Consts.bucketCopy, cpKey, dstData, dstPut.getEtag());
//
//            CopyObjectOutput gotSrc = client.copyObjectTo(Consts.bucket, Consts.bucketCopy, cpKey, key);
//            assertDataSame(client, Consts.bucketCopy, cpKey, srcData, gotSrc.getEtag());
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//                client.deleteObject(Consts.bucketCopy, cpKey);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void copyObjectFrom(){
//        // special case
//
//        String srcData = RandomStringUtils.randomAlphanumeric(1024);
//        String dstData = RandomStringUtils.randomAlphanumeric(2048);
//        String key = "object-copy-from-"+System.currentTimeMillis();
//        String cpKey = "object-copy-from-data-"+System.currentTimeMillis();
//        try{
//            PutObjectOutput srcPut = client.putObject(Consts.bucketCopy, cpKey, new ByteArrayInputStream(srcData.getBytes()));
//            assertDataSame(client, Consts.bucketCopy, cpKey, srcData, srcPut.getEtag());
//
//            PutObjectOutput dstPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(dstData.getBytes()));
//            assertDataSame(client, Consts.bucket, key, dstData, dstPut.getEtag());
//
//            CopyObjectOutput got = client.copyObjectFrom(Consts.bucket, Consts.bucketCopy, cpKey, key);
//            assertDataSame(client, Consts.bucket, key, srcData, got.getEtag());
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            Assert.assertNotNull(client);
//            Assert.assertNotNull(client);
//            try{
//                client.deleteObject(Consts.bucketCopy, cpKey);
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void copyRangeTest(){
//        String cr = TOSClient.copyRange(0, 0);
//        Assert.assertEquals("", cr);
//        long partSize = 1024L;
//        cr = TOSClient.copyRange(0, partSize);
//        Assert.assertEquals("bytes=0-1023", cr);
//        long startOffset = 128L;
//        cr = TOSClient.copyRange(startOffset, partSize);
//        Assert.assertEquals("bytes=128-1151", cr);
//        cr = TOSClient.copyRange(startOffset, 0);
//        Assert.assertEquals("bytes=128-", cr);
//    }
//
//    @Test
//    void uploadPartCopyTest(){
//        String srcData = RandomStringUtils.randomAlphanumeric(10*1024*1024+888);
//        String key = "object-upload-part-copy-"+System.currentTimeMillis();
//        String dstKey = "objectUploadPartCopy.data";
//        try{
//            PutObjectOutput srcPut = client.putObject(Consts.bucket, key, new ByteArrayInputStream(srcData.getBytes()));
//            assertDataSame(client, Consts.bucket, key, srcData, srcPut.getEtag());
//
//            CreateMultipartUploadOutput upload = client.createMultipartUpload(Consts.bucketCopy, dstKey);
//
//            HeadObjectOutput head = client.headObject(Consts.bucket, key);
//            long size = head.getObjectMeta().getContentLength();
//            int partSize = 5 * 1024 * 1024; // 5MB
//            int partCount = (int) (size / partSize);
//
//            UploadPartCopyOutput[] copyParts = new UploadPartCopyOutput[partCount];
//            for (int i = 0; i < partCount; i++) {
//                long partLen = partSize;
//                if (partCount == i + 1 && (size % partLen) > 0) {
//                    partLen += size % (long) partSize;
//                }
//                long startOffset = (long) i * partSize;
//
//                UploadPartCopyInput input = new UploadPartCopyInput().setUploadID(upload.getUploadID())
//                        .setDestinationKey(dstKey).setSourceBucket(Consts.bucket).setStartOffset(startOffset)
//                        .setPartSize(partLen).setPartNumber(i+1).setSourceKey(key);
//                copyParts[i] = client.uploadPartCopy(Consts.bucketCopy, input);
//            }
//
//            MultipartUploadedPart[] part = new MultipartUploadedPart[copyParts.length];
//            System.arraycopy(copyParts, 0, part, 0, part.length);
//
//            client.completeMultipartUpload(Consts.bucketCopy, new CompleteMultipartUploadInput(dstKey, upload.getUploadID(), part));
//
//            try(GetObjectOutput got = client.getObject(Consts.bucketCopy, dstKey)){
//                Assert.assertEquals(srcData.length(), got.getObjectMeta().getContentLength());
//                Assert.assertEquals(srcData, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//            }
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//                client.deleteObject(Consts.bucketCopy, dstKey);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    private void assertDataSame(TOSClient client, String bucket, String object, String data, String etag){
//        try{
//            HeadObjectOutput head = client.headObject(bucket, object);
//            Assert.assertEquals(etag, head.getObjectMeta().getEtags());
//            Assert.assertEquals(data.length(), head.getObjectMeta().getContentLength());
//
//            GetObjectOutput got = client.getObject(bucket, object);
//            String content = IOUtils.toString(got.getContent(), Charset.defaultCharset());
//            Assert.assertEquals(data, content);
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }
//    }
//
//    @Test
//    void multipartTest(){
//        String key = "multipart-test-"+System.currentTimeMillis();
//        byte[] data = RandomStringUtils.randomAlphanumeric(5 << 20).getBytes(StandardCharsets.UTF_8);
//        CreateMultipartUploadOutput upload = null;
//        try{
//            upload = client.createMultipartUpload(Consts.bucket, key);
//            UploadPartInput input1 = new UploadPartInput(key, upload.getUploadID(), data.length, 1,
//                    new TosObjectInputStream(new ByteArrayInputStream(data)));
//            UploadPartOutput part1 = client.uploadPart(Consts.bucket, input1);
//            UploadPartInput input2 = new UploadPartInput(key, upload.getUploadID(), data.length, 2,
//                    new TosObjectInputStream(new ByteArrayInputStream(data)));
//            UploadPartOutput part2 = client.uploadPart(Consts.bucket, input2);
//            ListUploadedPartsOutput parts = client.listUploadedParts(Consts.bucket, new ListUploadedPartsInput()
//                    .setKey(key).setUploadID(upload.getUploadID()));
//            Assert.assertEquals(2, parts.getUploadedParts().length);
//            CompleteMultipartUploadInput input = new CompleteMultipartUploadInput(key, upload.getUploadID(),
//                    new MultipartUploadedPart[]{part1, part2});
//            client.completeMultipartUpload(Consts.bucket, input);
//            client.deleteObject(Consts.bucket, key);
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }
//        Assert.assertNotNull(upload);
//        try{
//            client.abortMultipartUpload(Consts.bucket, new AbortMultipartUploadInput(key, upload.getRequestInfo().getRequestId()));
//        } catch (TosException e){
//            Assert.assertEquals(e.getCode(), "NotFound");
//        }
//    }
//
//    @Test
//    void multipartAbortTest(){
//        String key = "multipart-test-"+System.currentTimeMillis();
//        AbortMultipartUploadInput abort = null;
//        try {
//            CreateMultipartUploadOutput upload = client.createMultipartUpload(Consts.bucket, key);
//            abort = new AbortMultipartUploadInput(key, upload.getUploadID());
//            client.abortMultipartUpload(Consts.bucket, abort);
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }
//        Assert.assertNotNull(abort);
//        try{
//            client.abortMultipartUpload(Consts.bucket, abort);
//        } catch (TosException e){
//            Assert.assertEquals(e.getCode(), "NotFound");
//        }
//    }
//
//    @Test
//    void objectCURDTest(){
//        testObjectCrud(client, Consts.bucket);
//    }
//
//    static void testObjectCrud(TOSClient clientCRUD, String bucket){
//        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
//        InputStream stream = new ByteArrayInputStream(data.getBytes());
//        String key = "object-%E4%B8%AD%E6%96%87-curd-"+System.currentTimeMillis();
//        try{
//            PutObjectOutput put = clientCRUD.putObject(bucket, key, stream);
//            GetObjectOutput got = clientCRUD.getObject(bucket, key);
//            // NOTICE: 注意在对象很大的时候不要这样一次性读取
//            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//
//            HeadObjectOutput head = clientCRUD.headObject(bucket, key);
//            Assert.assertNotNull(head.getObjectMeta().getEtags());
//            Assert.assertEquals(head.getObjectMeta().getEtags(), got.getObjectMeta().getEtags());
//            Assert.assertEquals(head.getObjectMeta(), got.getObjectMeta());
//        } catch (TosException | IOException e) {
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                clientCRUD.deleteObject(bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void setObjectMetaTest(){
//        String data = RandomStringUtils.randomAlphanumeric(1024);
//        String key = "object-meta-"+System.currentTimeMillis();
//        try{
//            PutObjectOutput put = client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))
//                    , RequestOptions.withContentType("image"));
//            GetObjectOutput got = client.getObject(Consts.bucket, key);
//            // NOTICE: 注意在对象很大的时候不要这样一次性读取
//            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//
//            HeadObjectOutput head = client.headObject(Consts.bucket, key);
//            Assert.assertEquals("image", head.getObjectMeta().getContentType());
//
//            client.setObjectMeta(Consts.bucket, key, RequestOptions.withContentType("video"));
//            head = client.headObject(Consts.bucket, key);
//            Assert.assertEquals("video", head.getObjectMeta().getContentType());
//            got = client.getObject(Consts.bucket, key);
//            Assert.assertEquals(data, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//            Assert.assertEquals("video", got.getObjectMeta().getContentType());
//
//            client.setObjectMeta(Consts.bucket, key, RequestOptions.withContentType("image/png"));
//            head = client.headObject(Consts.bucket, key);
//            Assert.assertEquals("image/png", head.getObjectMeta().getContentType());
//            got = client.getObject(Consts.bucket, key);
//            Assert.assertEquals(data, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//            Assert.assertEquals("image/png", got.getObjectMeta().getContentType());
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void objectListTest(){
//        String objectPrefix = "object-"+System.currentTimeMillis() + "/";
//        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
//        int number = 12;
//        boolean hasMore = true;
//        String startAfter = "";
//        try{
//            for (int i = 0; i < number; i++) {
//                PutObjectOutput put = client.putObject(Consts.bucket, objectPrefix + i, new ByteArrayInputStream(data.getBytes()));
//                GetObjectOutput got = client.getObject(Consts.bucket, objectPrefix + i);
//                // NOTICE: 注意在对象很大的时候不要这样一次性读取
//                Assert.assertNotNull(put.getEtag());
//                Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//                Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//            }
//            for (int i = 0; i < 10 && hasMore; i++) {
//                ListObjectsInput input = new ListObjectsInput().setMarker(startAfter).setMaxKeys(1).setPrefix(objectPrefix);
//                ListObjectsOutput resp = client.listObjects(Consts.bucket, input);
//                hasMore = resp.isTruncated();
//                startAfter = resp.getNextMarker();
//            }
//            // reverse
//            for (int i = 0; i < 10 && hasMore; i++) {
//                ListObjectsInput input = new ListObjectsInput().setMarker(startAfter).setMaxKeys(1).setReverse(true);
//                ListObjectsOutput resp = client.listObjects(Consts.bucket, input);
//                hasMore = resp.isTruncated();
//                startAfter = resp.getNextMarker();
//            }
//        } catch (TosException | IOException e) {
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }finally {
//            try{
//                for (int i = 0; i < number; i++) {
//                    client.deleteObject(Consts.bucket, objectPrefix+i);
//                }
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void objectListVersionsTest(){
//        String bucketName = Consts.bucket;
//        int number = 12;
//        String key = "object-list-version-"+System.currentTimeMillis();
//        String[] versionID = new String[number];
//        try{
//            String buf = RandomStringUtils.randomAlphanumeric(1024);
//            boolean hasMore = true;
//            String startAfter = "";
//            for (int i = 0; i < number; i++) {
//                PutObjectOutput put = client.putObject(bucketName, key, new ByteArrayInputStream(buf.getBytes(StandardCharsets.UTF_8)));
//                versionID[i] = put.getVersionID();
//
//                GetObjectOutput got = client.getObject(bucketName, key, RequestOptions.withVersionID(put.getVersionID()));
//                Assert.assertEquals(buf, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//                Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//                Assert.assertEquals(put.getVersionID(), got.getObjectMeta().getVersionID());
//            }
//            for (int i = 0; i < 10 && hasMore; i++) {
//                ListObjectsInput input = new ListObjectsInput().setMarker(startAfter).setMaxKeys(1);
//                ListObjectsOutput resp = client.listObjects(bucketName, input);
//                hasMore = resp.isTruncated();
//                startAfter = resp.getNextMarker();
//            }
//            String versionIDMarker = versionID[0];
//            hasMore = true;
//            for (int i = 0; i < 10 && hasMore; i++) {
//                ListObjectVersionsInput input = new ListObjectVersionsInput().setKeyMarker(startAfter).setVersionIDMarker(versionIDMarker);
//                ListObjectVersionsOutput resp = client.listObjectVersions(bucketName, input);
//                hasMore = resp.isTruncated();
//                versionIDMarker = resp.getVersionIDMarker();
//            }
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }finally {
//            try{
//                for (int i = 0; i < number; i++) {
//                    client.deleteObject(bucketName, key, RequestOptions.withVersionID(versionID[i]));
//                }
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void appendObjectTest(){
//        String key = "append-object-"+System.currentTimeMillis();
//        String data = RandomStringUtils.randomAlphanumeric(128 << 10);
//        try{
//            AppendObjectOutput result = client.appendObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()), 0);
//            GetObjectOutput got = client.getObject(Consts.bucket, key);
//            // NOTICE: 注意在对象很大的时候不要这样一次性读取
//            Assert.assertEquals(crc32Check(data.getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//
//            String data2 = RandomStringUtils.randomAlphanumeric(256 << 10);
//            client.appendObject(Consts.bucket, key, new ByteArrayInputStream(data2.getBytes()), result.getNextAppendOffset());
//            got = client.getObject(Consts.bucket, key);
//            // NOTICE: 注意在对象很大的时候不要这样一次性读取
//            Assert.assertEquals(data.length()+data2.length(), got.getObjectMeta().getContentLength());
//            Assert.assertEquals(crc32Check((data+data2).getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        }finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void deleteMultiObjectsTest(){
//        String data = RandomStringUtils.randomAlphanumeric(1024);
//        String objectPrefix = "delete-multi-objects-"+System.currentTimeMillis();
//        int number = 12;
//        ObjectTobeDeleted[] objectTobeDeleteds = new ObjectTobeDeleted[number];
//        try{
//            for (int i = 0; i < number; i++) {
//                PutObjectOutput put = client.putObject(Consts.bucket, objectPrefix+i, new ByteArrayInputStream(data.getBytes()));
//                GetObjectOutput got = client.getObject(Consts.bucket, objectPrefix+i);
//                Assert.assertEquals(data, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//                Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//                objectTobeDeleteds[i] = new ObjectTobeDeleted().setKey(objectPrefix+i);
//            }
//            DeleteMultiObjectsOutput multiGot = client.deleteMultiObjects(Consts.bucket, new DeleteMultiObjectsInput(objectTobeDeleteds, false));
//            Assert.assertNotNull(multiGot);
//            Assert.assertNull(multiGot.getErrors());
//            Assert.assertEquals(number, multiGot.getDeleteds().length);
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try {
//                for (int i = 0; i < number; i++) {
//                    client.deleteObject(Consts.bucket, objectPrefix + i);
//                }
//            }catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void getObjectRangeTest(){
//        String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
//        InputStream stream = new ByteArrayInputStream(data.getBytes());
//        String key = "object-curd-"+System.currentTimeMillis();
//        try{
//            PutObjectOutput put = client.putObject(Consts.bucket, key, stream);
//            GetObjectOutput got = client.getObject(Consts.bucket, key, RequestOptions.withRange(0, 7));
//            // NOTICE: 注意在对象很大的时候不要这样一次性读取
//            Assert.assertEquals(crc32Check(data.substring(0, 8).getBytes()), crc32Check(IOUtils.toByteArray(got.getContent())));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//        } catch (TosException | IOException e) {
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            try{
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    static int crc32Check(byte[] data){
//        CRC32 crc = new CRC32();
//        crc.reset();
//        crc.update(data);
//        return (int) crc.getValue();
//    }
//
//    @Test
//    void objectAclTest(){
//        String key = "object-acl-"+System.currentTimeMillis();
//        try{
//
//            String data = RandomStringUtils.randomAlphanumeric(1024);
//            PutObjectOutput put = client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()));
//            GetObjectOutput got = client.getObject(Consts.bucket, key);
//            Assert.assertEquals(data, IOUtils.toString(got.getContent(), Charset.defaultCharset()));
//            Assert.assertEquals(put.getEtag(), got.getObjectMeta().getEtags());
//
//            GetObjectAclOutput gotAcl = client.getObjectAcl(Consts.bucket, key);
//            com.volcengine.tos.model.acl.Grant[] grants = new com.volcengine.tos.model.acl.Grant[1];
//            grants[0] = new com.volcengine.tos.model.acl.Grant().setPermission(ACLConst.PERMISSION_TYPE_WRITE_ACP)
//                    .setGrantee(new com.volcengine.tos.model.acl.Grantee().setId(gotAcl.getGrants()[0].getGrantee().getId())
//                            .setType(gotAcl.getGrants()[0].getGrantee().getType()));
//
//            // must set owner, add checker
//            PutObjectAclInput input = new PutObjectAclInput().setKey(key)
//                    .setAclRules(new ObjectAclRules().setGrants(grants).setOwner(new Owner()));
////            System.out.println(Consts.JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(input));
//            client.putObjectAcl(Consts.bucket, input);
//
//            gotAcl = client.getObjectAcl(Consts.bucket, key);
//            Assert.assertEquals(1, gotAcl.getGrants().length);
//            Assert.assertEquals(ACLConst.PERMISSION_TYPE_WRITE_ACP, gotAcl.getGrants()[0].getPermission());
//        } catch (TosException | IOException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            Assert.assertNotNull(client);
//            try{
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//
//    @Test
//    void putObjectPrivateACLTest(){
//        String key = "object-acl-"+System.currentTimeMillis();
//        try {
//            String data = RandomStringUtils.randomAlphanumeric(1024);
//            client.putObject(Consts.bucket, key, new ByteArrayInputStream(data.getBytes()));
//            PutObjectAclInput input = new PutObjectAclInput().setKey(key)
//                    .setAclGrant(new ObjectAclGrant().setAcl(ACLConst.ACL_PRIVATE));
//            client.putObjectAcl(Consts.bucket, input);
//
//            GetObjectAclOutput gotAcl = client.getObjectAcl(Consts.bucket, key);
//            Assert.assertEquals(1, gotAcl.getGrants().length);
//        } catch (TosException e){
//            Consts.LOG.error(e.toString(), e);
//            Assert.fail();
//        } finally {
//            Assert.assertNotNull(client);
//            try{
//                client.deleteObject(Consts.bucket, key);
//            } catch (TosException e){
//                Consts.LOG.error(e.toString(), e);
//                Assert.fail();
//            }
//        }
//    }
//}
