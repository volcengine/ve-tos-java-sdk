package com.volcengine.tos;

import com.volcengine.tos.comm.common.*;
import com.volcengine.tos.credential.StaticCredentialsProvider;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.zip.CheckedInputStream;

public class DirectoryBucketTest {
    private static TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
            .credentialsProvider(new StaticCredentialsProvider(Consts.accessKey, Consts.secretKey)).build());

    @Test
    void testBasicBucketObjectCrud() throws IOException {
        String bucket = TosUtils.genUuid();
        try {
            CreateBucketV2Output coutput = client.createBucket(new CreateBucketV2Input().setBucket(bucket).setBucketType(BucketType.BUCKET_TYPE_HNS));
            Assert.assertTrue(coutput.getRequestInfo().getRequestId().length() > 0);

            HeadBucketV2Output houtput = client.headBucket(new HeadBucketV2Input().setBucket(bucket));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertNotNull(houtput.getBucketType());
            Assert.assertEquals(houtput.getBucketType(), BucketType.BUCKET_TYPE_HNS);

            ListBucketsV2Output loutput = client.listBuckets(new ListBucketsV2Input());
            Assert.assertTrue(loutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertTrue(loutput.getBuckets().size() > 0);
            boolean found = false;
            for (ListedBucket b : loutput.getBuckets()) {
                if (b.getName().equals(bucket)) {
                    Assert.assertNotNull(b.getBucketType());
                    Assert.assertEquals(b.getBucketType(), BucketType.BUCKET_TYPE_HNS);
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);

            String key = "directory-bucket/" + System.currentTimeMillis();
            Map<String, String> meta = new HashMap<>();
            meta.put("key1", "value1");
            meta.put("中文键", "中文值");
            PutObjectOutput poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                    .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length())
                    .setOptions(new ObjectMetaRequestOptions().setCustomMetadata(meta)));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            HeadObjectV2Output hhoutput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(key));
            Assert.assertTrue(hhoutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(hhoutput.getContentLength(), "helloworld".length());
            Assert.assertEquals(hhoutput.getEtag(), poutput.getEtag());
            Assert.assertEquals(hhoutput.getHashCrc64ecma(), poutput.getHashCrc64ecma());
            Assert.assertEquals(hhoutput.getCustomMetadata().size(), 2);
            Assert.assertEquals(hhoutput.getCustomMetadata().get("key1"), "value1");
            Assert.assertEquals(hhoutput.getCustomMetadata().get("中文键"), "中文值");

            GetObjectV2Output goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(goutput.getContentLength(), "helloworld".length());
            Assert.assertEquals(goutput.getEtag(), poutput.getEtag());
            Assert.assertEquals(goutput.getHashCrc64ecma(), poutput.getHashCrc64ecma());
            Assert.assertEquals(goutput.getCustomMetadata().size(), 2);
            Assert.assertEquals(goutput.getCustomMetadata().get("key1"), "value1");
            Assert.assertEquals(goutput.getCustomMetadata().get("中文键"), "中文值");
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            try {
                client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket)
                        .setFetchMeta(true).setPrefix("directory-bucket/"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
                Assert.assertEquals(ex.getCode(), "InvalidDelimiter");
            }

            ListObjectsType2Output lloutput = client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket)
                    .setFetchMeta(true).setPrefix("directory-bucket/").setDelimiter("/").setMaxKeys(1000));
            Assert.assertTrue(lloutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertTrue(lloutput.getContents().size() >= 1);
            found = false;
            for (ListedObjectV2 obj : lloutput.getContents()) {
                if (obj.getKey().equals(key)) {
                    Assert.assertEquals(obj.getEtag(), goutput.getEtag());
                    Assert.assertEquals(obj.getHashCrc64ecma(), goutput.getHashCrc64ecma());
                    Assert.assertEquals(obj.getMeta().size(), 2);
                    Assert.assertEquals(obj.getMeta().get("key1"), "value1");
                    Assert.assertEquals(obj.getMeta().get("中文键"), "中文值");
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);

            String key2 = "directory-bucket/" + System.currentTimeMillis();
            CopyObjectV2Output ccoutput = client.copyObject(new CopyObjectV2Input()
                    .setBucket(bucket).setKey(key2).setSrcBucket(bucket).setSrcKey(key));
            Assert.assertTrue(ccoutput.getRequestInfo().getRequestId().length() > 0);

            goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key2));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(goutput.getContentLength(), "helloworld".length());
            Assert.assertEquals(goutput.getEtag(), poutput.getEtag());
            Assert.assertEquals(goutput.getHashCrc64ecma(), poutput.getHashCrc64ecma());
            Assert.assertEquals(goutput.getCustomMetadata().size(), 2);
            Assert.assertEquals(goutput.getCustomMetadata().get("key1"), "value1");
            Assert.assertEquals(goutput.getCustomMetadata().get("中文键"), "中文值");
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            DeleteObjectOutput doutput = client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(key2));
            Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key2));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
                Assert.assertEquals(ex.getCode(), "NoSuchKey");
            }


            String key3 = "directory-bucket/" + System.currentTimeMillis();
            CreateMultipartUploadOutput cccoutput = client.createMultipartUpload(new CreateMultipartUploadInput()
                    .setBucket(bucket).setKey(key3).setOptions(new ObjectMetaRequestOptions().setCustomMetadata(meta)));
            Assert.assertTrue(cccoutput.getRequestInfo().getRequestId().length() > 0);
            String uploadId = cccoutput.getUploadID();
            Assert.assertTrue(uploadId.length() > 0);

            String sampleFilePath = "src/test/resources/uploadPartTest.zip";
            File f = new File(sampleFilePath);
            long partSize = 5 * 1024 * 1024;
            long lastPartSize = f.length() % partSize;
            long partCount = lastPartSize == 0 ? f.length() / partSize : f.length() / partSize + 1;
            List<UploadedPartV2> uploadedParts = new ArrayList<>((int) partCount);
            for (int i = 0; i < partCount; i++) {
                FileInputStream fis = new FileInputStream(sampleFilePath);
                long contentLength = i == partCount - 1 ? (lastPartSize > 0 ? lastPartSize : partSize) : partSize;
                fis.skip(i * partSize);
                UploadPartV2Output uoutput = client.uploadPart(new UploadPartV2Input().setBucket(bucket).setKey(key3)
                        .setUploadID(uploadId).setPartNumber(i + 1)
                        .setContent(fis).setContentLength(contentLength));
                Assert.assertTrue(uoutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertTrue(uoutput.getEtag().length() > 0);
                uploadedParts.add(new UploadedPartV2().setPartNumber(i + 1).setEtag(uoutput.getEtag()));
                fis.close();
            }
            CompleteMultipartUploadV2Output ccccoutput = client.completeMultipartUpload(new CompleteMultipartUploadV2Input().setBucket(bucket).setKey(key3)
                    .setUploadID(uploadId).setUploadedParts(uploadedParts));
            Assert.assertTrue(ccccoutput.getRequestInfo().getRequestId().length() > 0);

            String key4 = "directory-bucket/" + System.currentTimeMillis();
            cccoutput = client.createMultipartUpload(new CreateMultipartUploadInput().setBucket(bucket)
                    .setKey(key4).setOptions(new ObjectMetaRequestOptions().setCustomMetadata(meta)));
            Assert.assertTrue(cccoutput.getRequestInfo().getRequestId().length() > 0);
            uploadId = cccoutput.getUploadID();
            Assert.assertTrue(uploadId.length() > 0);

            uploadedParts = new ArrayList<>((int) partCount);
            for (int i = 0; i < partCount; i++) {
                long end = i == partCount - 1 ? (lastPartSize > 0 ? lastPartSize : partSize) : partSize;
                UploadPartCopyV2Output uoutput = client.uploadPartCopy(new UploadPartCopyV2Input().setBucket(bucket).setKey(key4)
                        .setUploadID(uploadId).setPartNumber(i + 1)
                        .setSourceBucket(bucket).setSourceKey(key3)
                        .setCopySourceRange(i * partSize, i * partSize + end - 1));
                Assert.assertTrue(uoutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertTrue(uoutput.getEtag().length() > 0);
                uploadedParts.add(new UploadedPartV2().setPartNumber(i + 1).setEtag(uoutput.getEtag()));
            }
            ccccoutput = client.completeMultipartUpload(new CompleteMultipartUploadV2Input().setBucket(bucket).setKey(key4)
                    .setUploadID(uploadId).setUploadedParts(uploadedParts));
            Assert.assertTrue(ccccoutput.getRequestInfo().getRequestId().length() > 0);

            CRC64Checksum checksumTmp = new CRC64Checksum(0);
            CheckedInputStream inputStream = new CheckedInputStream(new FileInputStream(sampleFilePath), checksumTmp);
            byte[] tmp = new byte[8192];
            while (inputStream.read(tmp) != -1) {
            }
            String cliCrc = CRC64Utils.longToUnsignedLongString(inputStream.getChecksum().getValue());

            for (String k : Arrays.asList(key3, key4)) {
                hhoutput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(k));
                Assert.assertTrue(hhoutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertEquals(hhoutput.getCustomMetadata().size(), 2);
                Assert.assertEquals(hhoutput.getCustomMetadata().get("key1"), "value1");
                Assert.assertEquals(hhoutput.getCustomMetadata().get("中文键"), "中文值");
                Assert.assertEquals(hhoutput.getHashCrc64ecma(), cliCrc);
            }

            List<ObjectTobeDeleted> objects = new ArrayList<>(4);
            objects.add(new ObjectTobeDeleted().setKey(key));
            objects.add(new ObjectTobeDeleted().setKey(key2));
            objects.add(new ObjectTobeDeleted().setKey(key3));
            objects.add(new ObjectTobeDeleted().setKey(key4));
            objects.add(new ObjectTobeDeleted().setKey("directory-bucket/"));
            DeleteMultiObjectsV2Output ddoutput = client.deleteMultiObjects(new DeleteMultiObjectsV2Input().setBucket(bucket).setObjects(objects));
            Assert.assertTrue(ddoutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(ddoutput.getDeleteds().size(), 4);
            Assert.assertEquals(ddoutput.getErrors(), 1);

            try {
                client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
                Assert.assertEquals(ex.getCode(), "NoSuchKey");
            }
        } finally {
            this.cleanAndDeleteBucket(bucket);
        }
    }

    void deleteByCommonPrefix(String bucket, String prefix) {
        ListObjectsType2Input input = new ListObjectsType2Input().setBucket(bucket).setMaxKeys(1000).setDelimiter("/").setPrefix(prefix);
        while (true) {
            ListObjectsType2Output output = client.listObjectsType2(input);
            if (output.getCommonPrefixes() != null) {
                for (ListedCommonPrefix pre : output.getCommonPrefixes()) {
                    deleteByCommonPrefix(bucket, pre.getPrefix());
                    client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(pre.getPrefix()));
                }
            }

            if (output.getContents() != null) {
                for (ListedObjectV2 obj : output.getContents()) {
                    client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(obj.getKey()));
                }
            }

            if (!output.isTruncated()) {
                break;
            }
        }

    }

    void cleanAndDeleteBucket(String bucket) {
        try {
            client.headBucket(bucket);
        } catch (TosServerException ex) {
            if (ex.getStatusCode() == 404) {
                return;
            }
            throw ex;
        }

        ListMultipartUploadsV2Input input = new ListMultipartUploadsV2Input().setBucket(bucket).setMaxUploads(1000);
        while (true) {
            ListMultipartUploadsV2Output output = client.listMultipartUploads(input);
            if (output.getUploads() != null) {
                for (ListedUpload upload : output.getUploads()) {
                    client.abortMultipartUpload(new AbortMultipartUploadInput().setBucket(bucket).setUploadID(upload.getUploadID()).setKey(upload.getKey()));
                }
            }
            if (!output.isTruncated()) {
                break;
            }
            input.setKeyMarker(output.getNextKeyMarker());
            input.setUploadIDMarker(output.getNextUploadIdMarker());
        }

        this.deleteByCommonPrefix(bucket, null);
        client.deleteBucket(new DeleteBucketInput().setBucket(bucket));
    }

    @Test
    void testAppendObjectAndGetFileStatus() throws IOException {
        for (String bucket : Arrays.asList(Consts.bucket, TosUtils.genUuid())) {
            try {
                if (!bucket.equals(Consts.bucket)) {
                    CreateBucketV2Output coutput = client.createBucket(new CreateBucketV2Input().setBucket(bucket).setBucketType(BucketType.BUCKET_TYPE_HNS));
                    Assert.assertTrue(coutput.getRequestInfo().getRequestId().length() > 0);
                }

                // getFileStatus
                String prefix = TosUtils.genUuid() + "/";
                String key = prefix + System.currentTimeMillis();
                client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(key));
                client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(prefix));
                try {
                    client.getFileStatus(new GetFileStatusInput().setBucket(bucket).setKey(prefix));
                    Assert.assertTrue(false);
                } catch (TosServerException ex) {
                    Assert.assertEquals(ex.getStatusCode(), 404);
                }

                PutObjectOutput poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
                Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

                GetFileStatusOutput goutput = client.getFileStatus(new GetFileStatusInput().setBucket(bucket).setKey(prefix));
                Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
                if (bucket.equals(Consts.bucket)) {
                    Assert.assertEquals(goutput.getKey(), key);
                } else {
                    Assert.assertEquals(goutput.getKey(), prefix);
                }

                poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(prefix)
                        .setContent(null).setContentLength(0));
                Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

                goutput = client.getFileStatus(new GetFileStatusInput().setBucket(bucket).setKey(prefix));
                Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertEquals(goutput.getKey(), prefix);

                goutput = client.getFileStatus(new GetFileStatusInput().setBucket(bucket).setKey(key));
                Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertEquals(goutput.getKey(), key);

                GetObjectV2Output ggoutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
                Assert.assertTrue(ggoutput.getRequestInfo().getRequestId().length() > 0);
                CRC64Checksum checksumTmp = new CRC64Checksum(0);
                CheckedInputStream inputStream = new CheckedInputStream(ggoutput.getContent(), checksumTmp);
                byte[] tmp = new byte[8192];
                while (inputStream.read(tmp) != -1) {
                }
                ggoutput.getContent().close();
                Assert.assertEquals(CRC64Utils.longToUnsignedLongString(inputStream.getChecksum().getValue()), goutput.getCrc64());

                // appendObject
                String key2 = "append-object-" + System.currentTimeMillis();
                if (bucket.equals(Consts.bucket)) {
                    poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                            .setContent(new ByteArrayInputStream("hiworld".getBytes())));
                    Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

                    ggoutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
                    Assert.assertTrue(ggoutput.getRequestInfo().getRequestId().length() > 0);
                    Assert.assertEquals(StringUtils.toString(ggoutput.getContent(), "content"), "hiworld");
                    ggoutput.getContent().close();
                } else {
                    try {
                        client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                                .setContent(new ByteArrayInputStream("hiworld".getBytes())));
                        Assert.assertTrue(false);
                    } catch (TosServerException ex) {
                        Assert.assertEquals(ex.getStatusCode(), 405);
                    }
                    try {
                        client.appendObject(new AppendObjectInput().setBucket(bucket).setKey(key2)
                                .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length())
                                .setOptions(new ObjectMetaRequestOptions().setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD))
                                .setPreHashCrc64ecma("0")
                                .setOffset(0));
                        Assert.assertTrue(false);
                    } catch (TosException ex) {
                        Assert.assertEquals(ex.getStatusCode(), 404);
                    }
                }

                poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key2)
                        .setContent(null).setContentLength(0));
                Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

                AppendObjectOutput aoutput = client.appendObject(new AppendObjectInput().setBucket(bucket).setKey(key2)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length())
                        .setOptions(new ObjectMetaRequestOptions().setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD))
                        .setPreHashCrc64ecma("0")
                        .setOffset(0));
                Assert.assertTrue(aoutput.getRequestInfo().getRequestId().length() > 0);
                long nextOffset = aoutput.getNextAppendOffset();

                ggoutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key2));
                Assert.assertTrue(ggoutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertEquals(StringUtils.toString(ggoutput.getContent(), "content"), "helloworld");
                ggoutput.getContent().close();
                Assert.assertTrue(ggoutput.getHashCrc64ecma().length() > 0);

                aoutput = client.appendObject(new AppendObjectInput().setBucket(bucket).setKey(key2)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length())
                        .setOptions(new ObjectMetaRequestOptions().setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD))
                        .setPreHashCrc64ecma(ggoutput.getHashCrc64ecma())
                        .setOffset(nextOffset));
                Assert.assertTrue(aoutput.getRequestInfo().getRequestId().length() > 0);

                ggoutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key2));
                Assert.assertTrue(ggoutput.getRequestInfo().getRequestId().length() > 0);

                checksumTmp = new CRC64Checksum(0);
                inputStream = new CheckedInputStream(ggoutput.getContent(), checksumTmp);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int n;
                while ((n = inputStream.read(tmp)) != -1) {
                    bos.write(tmp, 0, n);
                }
                ggoutput.getContent().close();
                Assert.assertEquals(new String(bos.toByteArray()), "helloworldhelloworld");
                Assert.assertEquals(CRC64Utils.longToUnsignedLongString(inputStream.getChecksum().getValue()), ggoutput.getHashCrc64ecma());
            } finally {
                if (!bucket.equals(Consts.bucket)) {
                    this.cleanAndDeleteBucket(bucket);
                }
            }
        }
    }

    @Test
    void testModifyObjectAndDeleteFolder() throws IOException {
        String bucket = TosUtils.genUuid();
        try {
            CreateBucketV2Output coutput = client.createBucket(new CreateBucketV2Input().setBucket(bucket).setBucketType(BucketType.BUCKET_TYPE_HNS));
            Assert.assertTrue(coutput.getRequestInfo().getRequestId().length() > 0);

            String folder = TosUtils.genUuid() + "/";
            ListObjectsType2Output loutput = client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket).setPrefix(folder).setDelimiter("/"));
            Assert.assertTrue(loutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertTrue(loutput.getContents() == null || loutput.getContents().isEmpty());

            PutObjectOutput poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(folder).setContentLength(0));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            for (int i = 0; i < 3; i++) {
                String key = folder + "key" + i;
                PutObjectOutput ppoutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
                Assert.assertTrue(ppoutput.getRequestInfo().getRequestId().length() > 0);
            }

            GetObjectV2Output goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(folder + "key0"));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            TOSV2Client cli = (TOSV2Client) client;
            try {
                cli.modifyObject(new ModifyObjectInput().setBucket(bucket).setKey(folder + "key0")
                        .setOffset(1).setContentLength(1).setContent(new ByteArrayInputStream("a".getBytes())));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "OffsetNotMatched");
            }

            ModifyObjectOutput moutput = cli.modifyObject(new ModifyObjectInput().setBucket(bucket).setKey(folder + "key0")
                    .setOffset(goutput.getContentLength()).setContentLength("helloworld".length()).setContent(new ByteArrayInputStream("helloworld".getBytes())));
            Assert.assertTrue(moutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(moutput.getNextModifyOffset(), "helloworld".length() * 2);

            goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(folder + "key0"));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworldhelloworld");
            goutput.getContent().close();

            DeleteObjectOutput doutput = client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(folder + "key0"));
            Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(folder + "key0"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            try {
                client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(folder));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "CannotDelete");
            }

            for (int i = 0; i < 3; i++) {
                String key = folder + "key" + i;
                doutput = client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(key));
                Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);
            }

            doutput = client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(folder));
            Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);
            for (int i = 0; i < 3; i++) {
                String key = folder + "key" + i;
                try {
                    client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
                    Assert.assertTrue(false);
                } catch (TosServerException ex) {
                    Assert.assertEquals(ex.getStatusCode(), 404);
                }
            }

        } finally {
            this.cleanAndDeleteBucket(bucket);
        }
    }

    @Test
    void testUnsupportedApis() throws IOException {
        String bucket = TosUtils.genUuid();
        try {
            CreateBucketV2Output coutput = client.createBucket(new CreateBucketV2Input().setBucket(bucket).setBucketType(BucketType.BUCKET_TYPE_HNS));
            Assert.assertTrue(coutput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.listObjectVersions(new ListObjectVersionsV2Input().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.putBucketLifecycle(new PutBucketLifecycleInput().setBucket(bucket)
                        .setRules(Arrays.asList(new LifecycleRule().setId("1").setPrefix("prefix")
                                .setStatus(StatusType.STATUS_ENABLED).setExpiration(new Expiration().setDays(10)))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.getBucketLifecycle(new GetBucketLifecycleInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.deleteBucketLifecycle(new DeleteBucketLifecycleInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.putBucketStorageClass(new PutBucketStorageClassInput().setBucket(bucket).setStorageClass(StorageClassType.STORAGE_CLASS_IA));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.putBucketCORS(new PutBucketCORSInput().setBucket(bucket)
                        .setRules(Arrays.asList(new CORSRule().setResponseVary(true).setMaxAgeSeconds(300)
                                .setAllowedOrigins(Arrays.asList("*"))
                                .setAllowedHeaders(Arrays.asList("*"))
                                .setAllowedMethods(Arrays.asList("GET")).setExposeHeaders(Arrays.asList("x-tos-request-id")))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.getBucketCORS(new GetBucketCORSInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.deleteBucketCORS(new DeleteBucketCORSInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.putBucketMirrorBack(new PutBucketMirrorBackInput().setBucket(bucket)
                        .setRules(Arrays.asList(new MirrorBackRule().setId("1")
                                .setCondition(new Condition().setHttpCode(404).setKeyPrefix("prefix"))
                                .setRedirect(new Redirect().setRedirectType(RedirectType.REDIRECT_MIRROR)
                                        .setPublicSource(new PublicSource()
                                                .setSourceEndpoint(new SourceEndpoint()
                                                        .setPrimary(Arrays.asList("http://www.baidu.com"))))))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.getBucketMirrorBack(new GetBucketMirrorBackInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.deleteBucketMirrorBack(new DeleteBucketMirrorBackInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putBucketReplication(new PutBucketReplicationInput().setBucket(bucket)
                        .setRole("TosArchiveTOSInventory").setRules(Arrays.asList(new ReplicationRule()
                                .setStatus(StatusType.STATUS_ENABLED).setId("1")
                                .setDestination(new Destination().setBucket(Consts.bucket).setLocation(Consts.region)))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketReplication(new GetBucketReplicationInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketReplication(new DeleteBucketReplicationInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            try {
                client.putBucketVersioning(new PutBucketVersioningInput().setBucket(bucket).setStatus(VersioningStatusType.VERSIONING_STATUS_ENABLED));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketVersioning(new GetBucketVersioningInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putBucketWebsite(new PutBucketWebsiteInput().setBucket(bucket)
                        .setIndexDocument(new IndexDocument().setSuffix(".html")).setErrorDocument(new ErrorDocument().setKey("404.html")));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketWebsite(new GetBucketWebsiteInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketWebsite(new DeleteBucketWebsiteInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                FilterRule rule = new FilterRule().setName("prefix").setValue("dals");
                Filter filter = new Filter().setKey(new FilterKey().setRules(Collections.singletonList(rule)));
                CloudFunctionConfiguration configuration = new CloudFunctionConfiguration().setId("test-id").setCloudFunction("zkru2tzw")
                        .setEvents(Arrays.asList("tos:ObjectCreated:Put", "tos:ObjectDownload:*")).setFilter(filter);
                client.putBucketNotification(new PutBucketNotificationInput().setBucket(bucket)
                        .setCloudFunctionConfigurations(Arrays.asList(configuration)));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketNotification(new GetBucketNotificationInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                String domain = "www.volcengine.com";
                CustomDomainRule rule = new CustomDomainRule().setDomain(domain);
                client.putBucketCustomDomain(new PutBucketCustomDomainInput().setBucket(bucket).setRule(rule));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.listBucketCustomDomain(new ListBucketCustomDomainInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                String domain = "www.volcengine.com";
                client.deleteBucketCustomDomain(new DeleteBucketCustomDomainInput().setBucket(bucket).setDomain(domain));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                RealTimeLogConfiguration configuration = new RealTimeLogConfiguration().setRole("TOSLogArchiveTLSRole")
                        .setConfiguration(new AccessLogConfiguration().setUseServiceTopic(true));
                client.putBucketRealTimeLog(new PutBucketRealTimeLogInput().setBucket(bucket)
                        .setConfiguration(configuration));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketRealTimeLog(new GetBucketRealTimeLogInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketRealTimeLog(new DeleteBucketRealTimeLogInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putBucketRename(new PutBucketRenameInput().setBucket(bucket).setRenameEnable(true));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketRename(new GetBucketRenameInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketRename(new DeleteBucketRenameInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                PutBucketInventoryInput input = new PutBucketInventoryInput();
                input.setBucket(bucket);
                input.setId("1");
                input.setIsEnabled(true);
                input.setFilter(new BucketInventoryConfiguration.InventoryFilter().setPrefix("filter_prefix"));
                input.setIncludedObjectVersions(InventoryIncludedObjType.INVENTORY_INCLUDED_OBJ_TYPE_CURRENT);
                input.setDestination(new BucketInventoryConfiguration.InventoryDestination()
                        .setTosBucketDestination(new BucketInventoryConfiguration
                                .TOSBucketDestination().setBucket(Consts.bucket)
                                .setPrefix("destination_prefix").setFormat(InventoryFormatType.INVENTORY_FORMAT_CSV)
                                .setRole("TosArchiveTOSInventory").setAccountId("test-accountid")));
                input.setOptionalFields(new BucketInventoryConfiguration.InventoryOptionalFields().setField(Arrays.asList("Size", "ETag", "CRC64")));
                client.putBucketInventory(input);
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketInventory(new GetBucketInventoryInput().setBucket(bucket).setId("1"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketInventory(new DeleteBucketInventoryInput().setBucket(bucket).setId("1"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putBucketEncryption(new PutBucketEncryptionInput().setBucket(bucket)
                        .setRule(new BucketEncryptionRule()
                                .setApplyServerSideEncryptionByDefault(new BucketEncryptionRule.ApplyServerSideEncryptionByDefault().setSseAlgorithm("AES256"))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketEncryption(new GetBucketEncryptionInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.deleteBucketEncryption(new DeleteBucketEncryptionInput().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putBucketNotificationType2(new PutBucketNotificationType2Input()
                        .setBucket(bucket).setRules(Arrays.asList(new NotificationRule().setRuleId("1")
                                .setDestination(new NotificationDestination().setVeFaaS(Arrays.asList(new DestinationVeFaaS().setFunctionId("functionid")))))));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getBucketNotificationType2(new GetBucketNotificationType2Input().setBucket(bucket));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.fetchObject(new FetchObjectInput().setBucket(bucket).setKey("test-key").setUrl("http://www.baidu.com"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putFetchTask(new PutFetchTaskInput().setBucket(bucket).setKey("test-key").setUrl("http://www.baidu.com"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getFetchTask(new GetFetchTaskInput().setBucket(bucket).setTaskId("test-key"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.restoreObject(new RestoreObjectInput().setBucket(bucket).setKey("test-key")
                        .setRestoreJobParameters(new RestoreJobParameters().setTier(TierType.TIER_STANDARD)));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.putSymlink(new PutSymlinkInput().setBucket(bucket).setKey("test-key").setSymlinkTargetBucket(bucket).setSymlinkTargetKey("test-key2"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }
            try {
                client.getSymlink(new GetSymlinkInput().setBucket(bucket).setKey("test-key"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 405);
            }

            // presign policy signature
            PreSignedPolicyURLOutput output = client.preSignedPolicyURL(new PreSignedPolicyURLInput().setBucket(bucket).setExpires(3600)
                    .setConditions(Arrays.asList(new PolicySignatureCondition().setKey("key").setValue("prefix").setOperator("starts-with"))));
            Assert.assertTrue(output.getPreSignedPolicyURLGenerator().getSignedURLForList(null).length() > 0);
            OkHttpClient c = TosUtils.defaultOkHttpClient();
            Response r = c.newCall(new Request.Builder().url(output.getPreSignedPolicyURLGenerator()
                    .getSignedURLForList(null)).method("GET", null).build()).execute();
            Assert.assertEquals(r.code(), 400);
            // presign post signature
            String key = "post-signature-" + System.currentTimeMillis();
            PreSignedPostSignatureOutput poutput = client.preSignedPostSignature(new PreSignedPostSignatureInput().setBucket(bucket).setKey(key).setExpires(3600));
            Assert.assertTrue(poutput.getSignature().length() > 0);
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("key", key);
            builder.addFormDataPart("policy", poutput.getPolicy());
            builder.addFormDataPart("x-tos-algorithm", poutput.getAlgorithm());
            builder.addFormDataPart("x-tos-credential", poutput.getCredential());
            builder.addFormDataPart("x-tos-date", poutput.getDate());
            builder.addFormDataPart("x-tos-signature", poutput.getSignature());
            builder.addFormDataPart("file", key, RequestBody.create(MediaType.parse("text/plain"), "helloworld"));
            String endpoint = Consts.endpoint.toLowerCase();
            if (endpoint.startsWith("https://")) {
                endpoint = "https://" + bucket + "." + endpoint.substring("https://".length());
            } else if (endpoint.startsWith("http://")) {
                endpoint = "http://" + bucket + "." + endpoint.substring("http://".length());
            } else {
                endpoint = "http://" + bucket + "." + endpoint;
            }
            r = c.newCall(new Request.Builder().url(endpoint).post(builder.build()).build()).execute();
            Assert.assertEquals(r.code(), 400);

        } finally {
            this.cleanAndDeleteBucket(bucket);
        }
    }

    @Test
    void testUnsupportedParamsAndSpecialCases() throws IOException {
        String bucket = TosUtils.genUuid();
        String bucket2 = TosUtils.genUuid();
        try {
            CreateBucketV2Output coutput = client.createBucket(new CreateBucketV2Input().setBucket(bucket).setBucketType(BucketType.BUCKET_TYPE_HNS));
            Assert.assertTrue(coutput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.createBucket(new CreateBucketV2Input().setBucket(bucket2)
                        .setAzRedundancy(AzRedundancyType.AZ_REDUNDANCY_MULTI_AZ).setBucketType(BucketType.BUCKET_TYPE_HNS));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }

            try {
                client.createBucket(new CreateBucketV2Input().setBucket(bucket2)
                        .setStorageClass(StorageClassType.STORAGE_CLASS_IA).setBucketType(BucketType.BUCKET_TYPE_HNS));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }

            String folder = TosUtils.genUuid() + "/";
            String key = folder + "unsupported-params-" + System.currentTimeMillis();
            PutObjectOutput poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key)
                    .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            String etag = poutput.getEtag();
            if (etag.startsWith("\"")) {
                etag = etag.substring(1, etag.length());
            }
            if (etag.endsWith("\"")) {
                etag = etag.substring(0, etag.length() - 1);
            }
            String key2 = folder + "unsupported-params-" + System.currentTimeMillis();
            CopyObjectV2Output copyOutput = client.copyObject(new CopyObjectV2Input()
                    .setSrcBucket(bucket).setSrcKey(key)
                    .setBucket(bucket).setKey(key2)
                    .setTagging("tag1=value1&tag2=value2").setTaggingDirective(TaggingDirectiveType.TaggingDirectiveReplace).setCopySourceIfMatch(etag));
            Assert.assertTrue(copyOutput.getRequestInfo().getRequestId().length() > 0);
            GetObjectV2Output goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key2));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            GetObjectTaggingOutput ggoutput = client.getObjectTagging(new GetObjectTaggingInput().setBucket(bucket).setKey(key2));
            Assert.assertTrue(ggoutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(ggoutput.getTagSet().getTags().size(), 2);
            Assert.assertEquals(ggoutput.getTagSet().getTags().get(0).getKey(), "tag1");
            Assert.assertEquals(ggoutput.getTagSet().getTags().get(0).getValue(), "value1");

            try {
                client.copyObject(new CopyObjectV2Input()
                        .setSrcBucket(bucket).setSrcKey(folder)
                        .setBucket(bucket).setKey(key2));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "InvalidSourceOrDestinationResourceType");
            }

            String folder2 = TosUtils.genUuid() + "/";
            try {
                client.putObject(new PutObjectInput().setBucket(bucket).setKey(folder2)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
                Assert.assertEquals(ex.getCode(), "ContentLengthMustBeZero");
            }

            poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(folder2).setContentLength(0));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.copyObject(new CopyObjectV2Input()
                        .setSrcBucket(bucket).setSrcKey(key)
                        .setBucket(bucket).setKey(folder2));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "InvalidSourceOrDestinationResourceType");
            }

            String folder3 = TosUtils.genUuid() + "/";
            CopyObjectV2Output ccoutput = client.copyObject(new CopyObjectV2Input()
                    .setSrcBucket(bucket).setSrcKey(folder2)
                    .setBucket(bucket).setKey(folder3));
            Assert.assertTrue(ccoutput.getRequestInfo().getRequestId().length() > 0);

            HeadObjectV2Output houtput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder3));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);

            try {
                client.copyObject(new CopyObjectV2Input()
                        .setSrcBucket(bucket).setSrcKey(key)
                        .setBucket(bucket).setKey(key + "/"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "InvalidSourceOrDestinationResourceType");
            }

            DeleteMultiObjectsV2Output doutput = client.deleteMultiObjects(new DeleteMultiObjectsV2Input().setBucket(bucket)
                    .setObjects(Arrays.asList(new ObjectTobeDeleted().setKey(folder), new ObjectTobeDeleted().setKey(folder2))));
            Assert.assertTrue(doutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertTrue(doutput.getDeleteds() == null || doutput.getDeleteds().size() == 0);
            Assert.assertNotNull(doutput.getErrors());
            Assert.assertEquals(doutput.getErrors().size(), 2);
            Assert.assertEquals(doutput.getErrors().get(0).getCode(), "CannotDelete");
            Assert.assertEquals(doutput.getErrors().get(1).getCode(), "CannotDelete");

            goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            String versionId = "01234567890123456789";
            try {
                client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key).setVersionID(versionId));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }

            try {
                client.getObjectAcl(new GetObjectACLV2Input().setBucket(bucket).setKey(key).setVersionID(versionId));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }
            try {
                client.putObjectAcl(new PutObjectACLInput().setBucket(bucket).setKey(key).setVersionID(versionId).setAcl(ACLType.ACL_PUBLIC_READ));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }
            try {
                client.setObjectMeta(new SetObjectMetaInput().setBucket(bucket).setKey(key).setVersionID(versionId)
                        .setOptions(new ObjectMetaRequestOptions().setContentType("text/plain")));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
            }
            try {
                client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket).setDelimiter("#"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
                Assert.assertEquals(ex.getCode(), "InvalidDelimiter");
            }
            try {
                client.listObjects(new ListObjectsV2Input().setBucket(bucket).setDelimiter("#"));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
                Assert.assertEquals(ex.getCode(), "InvalidDelimiter");
            }

            try {
                client.putObject(new PutObjectInput()
                        .setBucket(bucket).setKey(folder2)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 400);
                Assert.assertEquals(ex.getCode(), "ContentLengthMustBeZero");
            }

            try {
                client.putObject(new PutObjectInput()
                        .setBucket(bucket).setKey(key + "/").setContentLength(0));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
                Assert.assertEquals(ex.getCode(), "DuplicateObject");
            }

            String key3 = folder3 + "subfolder/" + System.currentTimeMillis();
            poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(key3)
                    .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            houtput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder3.substring(0, folder3.length() - 1)));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(houtput.getContentLength(), 0);
            houtput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder3 + "subfolder"));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(houtput.getContentLength(), 0);

            try {
                client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder3 + "#" + System.currentTimeMillis()));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            // rename
            RenameObjectOutput routput = client.renameObject(new RenameObjectInput().setBucket(bucket).setKey(key3).setNewKey(key3 + "_renamed"));
            Assert.assertTrue(routput.getRequestInfo().getRequestId().length() > 0);
            goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key3 + "_renamed"));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            try {
                client.headObject(new HeadObjectV2Input()
                        .setBucket(bucket).setKey(key3));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            try {
                client.renameObject(new RenameObjectInput()
                        .setBucket(bucket).setKey(key3 + "_renamed").setNewKey(TosUtils.genUuid() + "/" + System.currentTimeMillis()));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            String folder4 = TosUtils.genUuid() + "/";
            poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(folder4)
                    .setContent(null).setContentLength(0));
            Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);

            String key4 = folder4 + System.currentTimeMillis();
            RenameObjectOutput rroutput = client.renameObject(new RenameObjectInput().setBucket(bucket).setKey(key).setNewKey(key4));
            Assert.assertTrue(rroutput.getRequestInfo().getRequestId().length() > 0);
            goutput = client.getObject(new GetObjectV2Input().setBucket(bucket).setKey(key4));
            Assert.assertTrue(goutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(StringUtils.toString(goutput.getContent(), "content"), "helloworld");
            goutput.getContent().close();

            try {
                client.headObject(new HeadObjectV2Input()
                        .setBucket(bucket).setKey(key));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            try {
                client.renameObject(new RenameObjectInput()
                        .setBucket(bucket).setKey(key4).setNewKey(TosUtils.genUuid() + "/" + System.currentTimeMillis()));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 404);
            }

            List<String> folder4Keys = new ArrayList<>(4);
            for (int i = 0; i < 3; i++) {
                String temp = folder4 + System.currentTimeMillis();
                folder4Keys.add(temp);
                poutput = client.putObject(new PutObjectInput().setBucket(bucket).setKey(temp)
                        .setContent(new ByteArrayInputStream("helloworld".getBytes())).setContentLength("helloworld".length()));
                Assert.assertTrue(poutput.getRequestInfo().getRequestId().length() > 0);
            }

            // rename folder
            try {
                client.renameObject(new RenameObjectInput()
                        .setBucket(bucket).setKey(folder4).setNewKey(folder3));
                Assert.assertTrue(false);
            } catch (TosServerException ex) {
                Assert.assertEquals(ex.getStatusCode(), 409);
            }

            folder4Keys.add(key4);
            String folder6 = TosUtils.genUuid() + "/";
            rroutput = client.renameObject(new RenameObjectInput()
                    .setBucket(bucket).setKey(folder4).setNewKey(folder6));
            Assert.assertTrue(rroutput.getRequestInfo().getRequestId().length() > 0);

            ListObjectsType2Output loutput = client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket).setDelimiter("/").setPrefix(folder4).setMaxKeys(1000));
            Assert.assertTrue(loutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertFalse(loutput.isTruncated());
            Assert.assertTrue(loutput.getContents() == null || loutput.getContents().size() == 0);

            loutput = client.listObjectsType2(new ListObjectsType2Input().setBucket(bucket).setDelimiter("/").setPrefix(folder6).setMaxKeys(1000));
            Assert.assertTrue(loutput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertFalse(loutput.isTruncated());
            Assert.assertEquals(loutput.getContents().size(), folder4Keys.size());
            for (ListedObjectV2 obj : loutput.getContents()) {
                Assert.assertTrue(folder4Keys.contains(obj.getKey().replaceAll(folder6, folder4)));
            }


            // createmultipart
            String folder5 = TosUtils.genUuid() + "/";
            String key5 = folder5 + "subfolder/" + System.currentTimeMillis();
            CreateMultipartUploadOutput cccoutput = client.createMultipartUpload(new CreateMultipartUploadInput()
                    .setBucket(bucket).setKey(key5));
            Assert.assertTrue(cccoutput.getRequestInfo().getRequestId().length() > 0);
            String uploadId = cccoutput.getUploadID();
            Assert.assertTrue(uploadId.length() > 0);

            String sampleFilePath = "src/test/resources/uploadPartTest.zip";
            File f = new File(sampleFilePath);
            long partSize = 5 * 1024 * 1024;
            long lastPartSize = f.length() % partSize;
            long partCount = lastPartSize == 0 ? f.length() / partSize : f.length() / partSize + 1;
            List<UploadedPartV2> uploadedParts = new ArrayList<>((int) partCount);
            for (int i = 0; i < partCount; i++) {
                FileInputStream fis = new FileInputStream(sampleFilePath);
                long contentLength = i == partCount - 1 ? (lastPartSize > 0 ? lastPartSize : partSize) : partSize;
                UploadPartV2Output uoutput = client.uploadPart(new UploadPartV2Input().setBucket(bucket).setKey(key5)
                        .setUploadID(uploadId).setPartNumber(i + 1)
                        .setContent(fis).setContentLength(contentLength));
                Assert.assertTrue(uoutput.getRequestInfo().getRequestId().length() > 0);
                Assert.assertTrue(uoutput.getEtag().length() > 0);
                uploadedParts.add(new UploadedPartV2().setPartNumber(i + 1).setEtag(uoutput.getEtag()));
                fis.close();
            }
            CompleteMultipartUploadV2Output ccccoutput = client.completeMultipartUpload(new CompleteMultipartUploadV2Input().setBucket(bucket).setKey(key5)
                    .setUploadID(uploadId).setUploadedParts(uploadedParts));
            Assert.assertTrue(ccccoutput.getRequestInfo().getRequestId().length() > 0);

            houtput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder5));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(houtput.getContentLength(), 0);
            houtput = client.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(folder5 + "subfolder/"));
            Assert.assertTrue(houtput.getRequestInfo().getRequestId().length() > 0);
            Assert.assertEquals(houtput.getContentLength(), 0);
        } finally {
            this.cleanAndDeleteBucket(bucket);
            this.cleanAndDeleteBucket(bucket2);
        }
    }
}
