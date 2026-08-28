package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.event.CopyEventType;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.event.DownloadEventType;
import com.volcengine.tos.comm.event.UploadEventType;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.object.*;
import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.volcengine.tos.Consts.LOG;
import static com.volcengine.tos.Consts.bucket;

public class TosFileRequestHandlerTest {
    private static final String sampleData = StringUtils.randomString(128 << 10);
    private static final String ssecKey = "Y2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2M=";
    private static final String ssecKeyMD5 = "ACdH+Fu9K3HlXdIUBu8GdA==";
    private static final String ssecAlgorithm = "AES256";
    private static final long sampleFileSize = 5 * 1024 * 1024;
    private static final String sampleFilePath = "src/test/resources/uploadPartTest.zip";
    private static final String smallFilePath = "src/test/resources/normalTest.jpg";
    private static final String emptyFilePath = "src/test/resources/emptyfile";
    private static final String notFound = "src/test/resources/xxx/yyy/zzz/uploadPartTest.zip";
    private static String sampleFileMD5 = null;
    private static final String smallFileMD5 = "0e12cc5493d2dd28e363408e23899ea4";

    private TosFileRequestHandler getHandler() {
        return ClientInstance.getFileRequestHandlerInstance();
    }

    private String getUniqueObjectKey() {
        return StringUtils.randomString(10);
    }

    @BeforeTest
    void init() throws InterruptedException {
        try {
            HeadBucketV2Output head = ClientInstance.getBucketRequestHandlerInstance()
                    .headBucket(HeadBucketV2Input.builder().bucket(com.volcengine.tos.Consts.bucket).build());
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try {
                    ClientInstance.getBucketRequestHandlerInstance().createBucket(
                            CreateBucketV2Input.builder().bucket(com.volcengine.tos.Consts.bucket).build()
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
            ClientInstance.getBucketRequestHandlerInstance().headBucket(HeadBucketV2Input.builder()
                    .bucket(com.volcengine.tos.Consts.bucketCopy).build());
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
    void uploadFileTest() {
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        UploadFileV2Input input = UploadFileV2Input.builder()
                .bucket(Consts.bucket).key(key)
                .filePath(sampleFilePath)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(5 * 1024 * 1024)
                .build();
        long contentLength = new File(sampleFilePath).length();
        input.setDataTransferListener(status -> {
            Assert.assertEquals(status.getTotalBytes(), contentLength);
            if (status.getType() == DataTransferType.DATA_TRANSFER_STARTED) {
                Assert.assertEquals(status.getConsumedBytes(), 0);
            }
            if (status.getType() == DataTransferType.DATA_TRANSFER_RW) {
                Assert.assertTrue(status.getConsumedBytes() <= status.getTotalBytes());
            }
            if (status.getType() == DataTransferType.DATA_TRANSFER_SUCCEED) {
                Assert.assertEquals(status.getConsumedBytes(), contentLength);
            }
            if (status.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                Assert.fail();
            }
        });
        AtomicInteger createMultipart = new AtomicInteger();
        AtomicInteger uploadPart = new AtomicInteger();
        AtomicInteger complete = new AtomicInteger();
        input.setUploadEventListener(((event) -> {
            if (event.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                createMultipart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                uploadPart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                complete.getAndIncrement();
            }
        }));
        UploadFileV2Output output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key);
        Assert.assertEquals(createMultipart.get(), 1);
        Assert.assertEquals(uploadPart.get(), 3);
        Assert.assertEquals(complete.get(), 1);
        getHandler().getObjectToFile(GetObjectToFileInput.builder().bucket(Consts.bucket).key(key)
                .filePath(sampleFilePath + ".1").build());

        try (FileInputStream inputStream = new FileInputStream(sampleFilePath + ".1")) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
        } catch (IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } finally {
            new File(sampleFilePath + ".1").delete();
        }

        // 0字节文件
        String emptyKey = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        input = UploadFileV2Input.builder()
                .bucket(Consts.bucket).key(emptyKey)
                .filePath(emptyFilePath)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(5 * 1024 * 1024)
                .build();
        input.setDataTransferListener(status -> {
            Assert.assertEquals(status.getTotalBytes(), 0L);
            Assert.assertEquals(status.getConsumedBytes(), 0L);
            if (status.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                Assert.fail();
            }
        });
        input.setUploadEventListener(((event) -> {
            if (event.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                createMultipart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                uploadPart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                complete.getAndIncrement();
            }
        }));
        output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), emptyKey);
        Assert.assertEquals(createMultipart.get(), 2);
        Assert.assertEquals(uploadPart.get(), 4);
        Assert.assertEquals(complete.get(), 2);

        HeadObjectV2Output headOutput = ClientInstance.getObjectRequestHandlerInstance().headObject(
                new HeadObjectV2Input().setBucket(Consts.bucket).setKey(emptyKey));
        Assert.assertEquals(headOutput.getContentLength(), 0L);

        // 文件大小为PartSize的整数倍
        long partSize = contentLength / 2;
        String key1 = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        input = UploadFileV2Input.builder().bucket(Consts.bucket).key(key1).filePath(sampleFilePath)
                .enableCheckpoint(true).taskNum(3).partSize(partSize).build();
        input.setUploadEventListener(((event) -> {
            if (event.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                createMultipart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                uploadPart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                complete.getAndIncrement();
            }
        }));
        output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key1);
        Assert.assertEquals(createMultipart.get(), 3);
        Assert.assertEquals(uploadPart.get(), 6);
        Assert.assertEquals(complete.get(), 3);

        getHandler().getObjectToFile(GetObjectToFileInput.builder().bucket(Consts.bucket).key(key1)
                .filePath(sampleFilePath + ".2").build());
        try (FileInputStream inputStream = new FileInputStream(sampleFilePath + ".2")) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
        } catch (IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } finally {
            new File(sampleFilePath + ".2").delete();
        }

        // 不足5M的文件
        String key2 = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        input = UploadFileV2Input.builder().bucket(Consts.bucket).key(key2).filePath(smallFilePath)
                .enableCheckpoint(true).taskNum(3).partSize(5 * 1024 * 1024).build();
        output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key2);

        getHandler().getObjectToFile(GetObjectToFileInput.builder().bucket(Consts.bucket).key(key2)
                .filePath(smallFilePath + ".1").build());
        try (FileInputStream inputStream = new FileInputStream(smallFilePath + ".1")) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), smallFileMD5);
        } catch (IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } finally {
            new File(smallFilePath + ".1").delete();
        }

        // callback
        try {
            input.setCallback("eyJjYWxsYmFja1VybCI6Imh0dHA6Ly9odHRwYmluLm9yZy9wb3N0In0=");
            UploadFileV2Output output1 = getHandler().uploadFile(input);
            Assert.assertNotNull(output1.getCallbackResult());
        } catch (TosServerException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NON_AUTHORITATIVE_INFO);
            Assert.assertEquals(e.getCode(), "CallbackFailed");
        }
    }

    @Test
    void uploadNullFileTest() {
        String filepath = sampleFilePath + System.currentTimeMillis();
        try {
            boolean created = new File(filepath).createNewFile();
            Assert.assertTrue(created);
        } catch (IOException e) {
            testFailed(e);
        }
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        UploadFileV2Input input = UploadFileV2Input.builder()
                .bucket(Consts.bucket).key(key)
                .filePath(filepath)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(5 * 1024 * 1024)
                .build();
        try {
            UploadFileV2Output output = getHandler().uploadFile(input);
            Assert.assertNotNull(output.getUploadID());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            new File(filepath).delete();
        }
    }

    @Test
    void putObjectFromFileTest() {
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        PutObjectFromFileInput input = PutObjectFromFileInput.builder()
                .filePath(sampleFilePath).bucket(bucket).key(key).build();
        try {
            PutObjectFromFileOutput output = getHandler().putObjectFromFile(input);
            Assert.assertNotNull(output.getPutObjectOutput());
        } catch (TosException e) {
            testFailed(e);
        }
        getHandler().getObjectToFile(GetObjectToFileInput.builder()
                .bucket(Consts.bucket)
                .key(key)
                .filePath(sampleFilePath + ".2")
                .build());
        try (FileInputStream inputStream = new FileInputStream(sampleFilePath + ".2")) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
        } catch (IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } finally {
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput()
                    .setBucket(Consts.bucket).setKey(key));
            new File(sampleFilePath + ".1").delete();
        }
    }

    @Test
    void downloadFileTest() {
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        String emptyKey = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        try {
            // upload data
            for (int i = 0; i < 2; i++) {
                PutObjectFromFileInput input = PutObjectFromFileInput.builder().filePath(sampleFilePath).
                        bucket(Consts.bucket).key(key).build();

                PutObjectFromFileOutput output = getHandler().putObjectFromFile(input);
                Assert.assertNotNull(output.getPutObjectOutput());

                // head it
                HeadObjectV2Output head = ClientInstance.getObjectRequestHandlerInstance()
                        .headObject(new HeadObjectV2Input().setBucket(Consts.bucket).setKey(key));
                long contentLength = head.getContentLength();
                Assert.assertEquals(contentLength, new File(sampleFilePath).length());

                DownloadFileInput downloadFileInput = DownloadFileInput.builder()
                        .bucket(Consts.bucket)
                        .key(key)
                        .filePath(notFound + "." + i)
                        .enableCheckpoint(true)
                        .taskNum(3)
                        .partSize(1024 * 1024 * 5L)
                        .build();

                // invalid part size
                try {
                    downloadFileInput.setPartSize(5L * 1024 * 1024 * 1024 + 1000);
                    getHandler().downloadFile(downloadFileInput);
                } catch (TosClientException e) {
                    Assert.assertTrue(e.getMessage().contains("invalid part size"));
                }
                try {
                    downloadFileInput.setPartSize(3L * 1024 * 1024);
                    getHandler().downloadFile(downloadFileInput);
                } catch (TosClientException e) {
                    Assert.assertTrue(e.getMessage().contains("invalid part size"));
                }
                downloadFileInput.setPartSize(5 * 1024 * 1024);

                // download not found object
                try {
                    downloadFileInput.setBucket(bucket).setKey(key + "notfound" + System.currentTimeMillis());
                    getHandler().downloadFile(downloadFileInput);
                    Assert.fail();
                } catch (TosException e) {
                    Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
                }

                downloadFileInput = DownloadFileInput.builder()
                        .bucket(Consts.bucket)
                        .key(key)
                        .filePath(notFound + "." + i)
                        .enableCheckpoint(i % 2 == 0)
                        .taskNum(3)
                        .partSize(1024 * 1024 * 5)
                        .build();
                AtomicInteger createTempFile = new AtomicInteger();
                AtomicInteger downloadPart = new AtomicInteger();
                AtomicInteger renameTempFile = new AtomicInteger();
                downloadFileInput.setDownloadEventListener(((event) -> {
                    if (event.getDownloadEventType() == DownloadEventType.DownloadEventCreateTempFileSucceed) {
                        createTempFile.getAndIncrement();
                    }
                    if (event.getDownloadEventType() == DownloadEventType.DownloadEventDownloadPartSucceed) {
                        downloadPart.getAndIncrement();
                    }
                    if (event.getDownloadEventType() == DownloadEventType.DownloadEventRenameTempFileSucceed) {
                        renameTempFile.getAndIncrement();
                    }
                }));
                downloadFileInput.setDataTransferListener(status -> {
                    Assert.assertEquals(status.getTotalBytes(), contentLength);
                    if (status.getType() == DataTransferType.DATA_TRANSFER_STARTED) {
                        Assert.assertEquals(status.getConsumedBytes(), 0);
                    }
                    if (status.getType() == DataTransferType.DATA_TRANSFER_RW) {
                        Assert.assertTrue(status.getConsumedBytes() <= status.getTotalBytes());
                    }
                    if (status.getType() == DataTransferType.DATA_TRANSFER_SUCCEED) {
                        Assert.assertEquals(status.getConsumedBytes(), contentLength);
                    }
                    if (status.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                        Assert.fail();
                    }
                });
                DownloadFileOutput downloadOutput = getHandler().downloadFile(downloadFileInput);
                Assert.assertEquals(downloadOutput.getContentLength(), contentLength);
                Assert.assertEquals(createTempFile.get(), 1);
                Assert.assertEquals(downloadPart.get(), 3);
                Assert.assertEquals(renameTempFile.get(), 1);

                try (FileInputStream inputStream = new FileInputStream(notFound + "." + i)) {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        md5.update(buffer, 0, length);
                    }
                    Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
                }

                // 下载文件为PartSize的整数倍
                long partSize = contentLength / 2;
                downloadFileInput = DownloadFileInput.builder()
                        .bucket(Consts.bucket)
                        .key(key)
                        .filePath(notFound + "." + "new" + i)
                        .enableCheckpoint(i % 2 == 0)
                        .taskNum(3)
                        .partSize(partSize)
                        .build();
                downloadOutput = getHandler().downloadFile(downloadFileInput);
                Assert.assertEquals(downloadOutput.getContentLength(), contentLength);

                try (FileInputStream inputStream = new FileInputStream(notFound + "." + "new" + i)) {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        md5.update(buffer, 0, length);
                    }
                    Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
                }

                // 下载文件为0字节
                input = PutObjectFromFileInput.builder().filePath(emptyFilePath).bucket(Consts.bucket).key(emptyKey).build();
                getHandler().putObjectFromFile(input);

                downloadFileInput = DownloadFileInput.builder()
                        .bucket(Consts.bucket)
                        .key(emptyKey)
                        .filePath(notFound + "." + "empty" + i)
                        .enableCheckpoint(i % 2 == 0)
                        .taskNum(3)
                        .partSize(partSize)
                        .build();
                downloadOutput = getHandler().downloadFile(downloadFileInput);
                Assert.assertEquals(downloadOutput.getContentLength(), 0);

                File emptyFile = new File(notFound + "." + "empty" + i);
                Assert.assertEquals(emptyFile.length(), 0);
            }
        } catch (IOException | NoSuchAlgorithmException | TosException e) {
            testFailed(e);
        } finally {
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput()
                    .setBucket(Consts.bucket).setKey(key));
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput()
                    .setBucket(Consts.bucket).setKey(emptyKey));
            File file = new File("src/test/resources/xxx");
            FileTest.deleteFileRecursive(file);
        }
    }

    @Test
    void abortUploadFileDownloadFileTest() throws InterruptedException {
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        UploadFileV2Input input = UploadFileV2Input.builder()
                .bucket(Consts.bucket).key(key)
                .filePath(sampleFilePath)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(5 * 1024 * 1024)
                .cancelHook(true)
                .build();
        AtomicInteger createMultipart = new AtomicInteger();
        AtomicInteger uploadPart = new AtomicInteger();
        AtomicInteger complete = new AtomicInteger();

        // 模拟上传中断，不 abort 任务
        input.setUploadEventListener((event) -> {
            if (event.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                createMultipart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                uploadPart.getAndIncrement();
                if (uploadPart.get() == 2 && input.getCancelHook() != null) {
                    input.getCancelHook().cancel(false);
                }
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                complete.getAndIncrement();
            }
        });
        UploadFileV2Output output = null;
        try {
            output = getHandler().uploadFile(input);
        } catch (TosException e) {
            Assert.assertTrue(e instanceof TosClientException);
            Assert.assertTrue(e.getCause() instanceof CancellationException);
            Assert.assertTrue(new File(input.getCheckpointFile()).exists());
            Assert.assertEquals(createMultipart.get(), 1);
            Assert.assertEquals(uploadPart.get(), 2);
            Assert.assertEquals(complete.get(), 0);
        } finally {
            createMultipart.set(0);
            uploadPart.set(0);
            complete.set(0);
        }

        Thread.sleep(100);
        createMultipart.set(0);
        uploadPart.set(0);
        complete.set(0);

        // 续传上次任务
        output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key);
        Assert.assertEquals(createMultipart.get(), 0);
        Assert.assertEquals(uploadPart.get(), 1);
        Assert.assertEquals(complete.get(), 1);
        Assert.assertFalse(new File(input.getCheckpointFile()).exists());

        // 模拟上传中断，abort 任务
        input.setUploadEventListener((event) -> {
            if (event.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                createMultipart.getAndIncrement();
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                uploadPart.getAndIncrement();
                if (uploadPart.get() == 2 && input.getCancelHook() != null) {
                    input.getCancelHook().cancel(true);
                }
            }
            if (event.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                complete.getAndIncrement();
            }
        });

        // reset cancel hook
        input.setCancelHook(true);
        createMultipart.set(0);
        uploadPart.set(0);
        complete.set(0);
        try {
            output = getHandler().uploadFile(input);
        } catch (TosException e) {
            Assert.assertTrue(e instanceof TosClientException);
            Assert.assertTrue(e.getCause() instanceof CancellationException);
            // will delete checkpoint
            Assert.assertEquals(createMultipart.get(), 1);
            Assert.assertEquals(uploadPart.get(), 2);
            Assert.assertEquals(complete.get(), 0);
        } finally {
            createMultipart.set(0);
            uploadPart.set(0);
            complete.set(0);
        }

        // 重新开始上传
        input.setCancelHook(false);
        Thread.sleep(1000);
        createMultipart.set(0);
        uploadPart.set(0);
        complete.set(0);

        new File(input.getCheckpointFile()).delete();

        output = getHandler().uploadFile(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key);
        Assert.assertEquals(createMultipart.get(), 1);
        Assert.assertEquals(uploadPart.get(), 3);
        Assert.assertEquals(complete.get(), 1);
        Assert.assertFalse(new File(input.getCheckpointFile()).exists());


        // download file
        String randomFilePathToDownload = sampleFilePath + StringUtils.randomString(3);
        AtomicInteger createTempFile = new AtomicInteger();
        AtomicInteger downloadPart = new AtomicInteger();
        AtomicInteger renameTempFile = new AtomicInteger();
        DownloadFileInput downloadFileInput = DownloadFileInput.builder()
                .bucket(Consts.bucket)
                .key(key)
                .filePath(randomFilePathToDownload)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(1024 * 1024 * 5L)
                .cancelHook(true)
                .build();

        // 模拟下载中断，不 abort 任务
        downloadFileInput.setDownloadEventListener((event) -> {
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventCreateTempFileSucceed) {
                createTempFile.getAndIncrement();
            }
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventDownloadPartSucceed) {
                downloadPart.getAndIncrement();
                if (downloadPart.get() == 2 && downloadFileInput.getCancelHook() != null) {
                    downloadFileInput.getCancelHook().cancel(false);
                }
            }
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventRenameTempFileSucceed) {
                renameTempFile.getAndIncrement();
            }
        });
        try {
            getHandler().downloadFile(downloadFileInput);
        } catch (TosException e) {
            Assert.assertTrue(e instanceof TosClientException);
            Assert.assertTrue(e.getCause() instanceof CancellationException);
            Assert.assertTrue(new File(downloadFileInput.getCheckpointFile()).exists());
            Assert.assertEquals(createTempFile.get(), 1);
            Assert.assertEquals(downloadPart.get(), 2);
            Assert.assertEquals(renameTempFile.get(), 0);
        } finally {
            createTempFile.set(0);
            downloadPart.set(0);
            renameTempFile.set(0);
        }
        Thread.sleep(1000);
        createTempFile.set(0);
        downloadPart.set(0);
        renameTempFile.set(0);

        // 继续下载上次任务
        getHandler().downloadFile(downloadFileInput);
        Assert.assertEquals(createTempFile.get(), 0);
        Assert.assertEquals(downloadPart.get(), 1);
        Assert.assertEquals(renameTempFile.get(), 1);
        Assert.assertFalse(new File(downloadFileInput.getCheckpointFile()).exists());

        // 模拟下载中断，abort 任务
        downloadFileInput.setDownloadEventListener((event) -> {
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventCreateTempFileSucceed) {
                createTempFile.getAndIncrement();
            }
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventDownloadPartSucceed) {
                downloadPart.getAndIncrement();
                if (downloadPart.get() == 2 && downloadFileInput.getCancelHook() != null) {
                    downloadFileInput.getCancelHook().cancel(true);
                }
            }
            if (event.getDownloadEventType() == DownloadEventType.DownloadEventRenameTempFileSucceed) {
                renameTempFile.getAndIncrement();
            }
        });

        // reset cancel hook
        downloadFileInput.setCancelHook(true);
        createTempFile.set(0);
        downloadPart.set(0);
        renameTempFile.set(0);

        try {
            getHandler().downloadFile(downloadFileInput);
        } catch (TosException e) {
            Assert.assertTrue(e instanceof TosClientException);
            Assert.assertTrue(e.getCause() instanceof CancellationException);
            // will delete checkpoint
            Assert.assertEquals(createTempFile.get(), 1);
            Assert.assertEquals(downloadPart.get(), 2);
            Assert.assertEquals(renameTempFile.get(), 0);
        } finally {
            createTempFile.set(0);
            downloadPart.set(0);
            renameTempFile.set(0);
        }

        // 重新下载
        downloadFileInput.setCancelHook(false);
        Thread.sleep(1000);
        createTempFile.set(0);
        downloadPart.set(0);
        renameTempFile.set(0);

        getHandler().downloadFile(downloadFileInput);
        Assert.assertEquals(createTempFile.get(), 1);
        Assert.assertEquals(downloadPart.get(), 3);
        Assert.assertEquals(renameTempFile.get(), 1);
        Assert.assertFalse(new File(downloadFileInput.getCheckpointFile()).exists());

        try (FileInputStream inputStream = new FileInputStream(randomFilePathToDownload)) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
        } catch (IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } finally {
            new File(randomFilePathToDownload).delete();
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput().setBucket(Consts.bucket).setKey(key));
        }
    }

    @Test
    void resumableCopyObjectTest() {
        String srcKey = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        String srcEmptyKey = Consts.internalFileCrudPrefix + getUniqueObjectKey();

        try {
            // upload src data
            PutObjectFromFileInput putFile = PutObjectFromFileInput.builder()
                    .filePath(sampleFilePath).bucket(Consts.bucket).key(srcKey).build();
            PutObjectFromFileOutput putOutput = getHandler().putObjectFromFile(putFile);
            Assert.assertNotNull(putOutput.getPutObjectOutput());

            // upload empty src data
            putFile = PutObjectFromFileInput.builder()
                    .filePath(emptyFilePath).bucket(Consts.bucket).key(srcEmptyKey).build();
            putOutput = getHandler().putObjectFromFile(putFile);
            Assert.assertNotNull(putOutput.getPutObjectOutput());

            ResumableCopyObjectInput input = ResumableCopyObjectInput.builder()
                    .bucket(Consts.bucket).key(key)
                    .enableCheckpoint(true)
                    .taskNum(3)
                    .partSize(5 * 1024 * 1024)
                    .srcBucket(bucket)
                    .srcKey(srcKey)
                    .checkpointFile(sampleFilePath + ".copy")
                    .build();
            AtomicInteger createMultipart = new AtomicInteger();
            AtomicInteger uploadPart = new AtomicInteger();
            AtomicInteger complete = new AtomicInteger();
            input.setCopyEventListener(((event) -> {
                if (event.getType() == CopyEventType.CopyEventCreateMultipartUploadSucceed) {
                    createMultipart.getAndIncrement();
                }
                if (event.getType() == CopyEventType.CopyEventUploadPartCopySucceed) {
                    uploadPart.getAndIncrement();
                }
                if (event.getType() == CopyEventType.CopyEventCompleteMultipartUploadSucceed) {
                    complete.getAndIncrement();
                }
            }));
            ResumableCopyObjectOutput output = getHandler().resumableCopyObject(input);
            Assert.assertEquals(output.getBucket(), Consts.bucket);
            Assert.assertEquals(output.getKey(), key);
            Assert.assertEquals(createMultipart.get(), 1);
            Assert.assertEquals(uploadPart.get(), 3);
            Assert.assertEquals(complete.get(), 1);
            GetObjectToFileOutput getObjectToFileOutput = getHandler().getObjectToFile(GetObjectToFileInput.builder()
                    .getObjectInputV2(GetObjectV2Input.builder()
                            .bucket(Consts.bucket)
                            .key(key).build())
                    .filePath(sampleFilePath + ".1")
                    .build());
            long contentLength = getObjectToFileOutput.getContentLength();

            try (FileInputStream inputStream = new FileInputStream(sampleFilePath + ".1")) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
            }

            // 源文件是partSize的整数倍
            long partSize = contentLength / 2;
            input = ResumableCopyObjectInput.builder().bucket(Consts.bucket).key(key).enableCheckpoint(true).taskNum(3).
                    checkpointFile(sampleFilePath + ".copy")
                    .partSize(partSize).srcBucket(bucket).srcKey(srcKey).build();
            input.setCopyEventListener(((event) -> {
                if (event.getType() == CopyEventType.CopyEventCreateMultipartUploadSucceed) {
                    createMultipart.getAndIncrement();
                }
                if (event.getType() == CopyEventType.CopyEventUploadPartCopySucceed) {
                    uploadPart.getAndIncrement();
                }
                if (event.getType() == CopyEventType.CopyEventCompleteMultipartUploadSucceed) {
                    complete.getAndIncrement();
                }
            }));
            output = getHandler().resumableCopyObject(input);
            Assert.assertEquals(output.getBucket(), Consts.bucket);
            Assert.assertEquals(output.getKey(), key);
            Assert.assertEquals(createMultipart.get(), 2);
            Assert.assertEquals(uploadPart.get(), 5);
            Assert.assertEquals(complete.get(), 2);

            getHandler().getObjectToFile(GetObjectToFileInput.builder().bucket(Consts.bucket)
                    .key(key).filePath(sampleFilePath + ".2").build());
            try (FileInputStream inputStream = new FileInputStream(sampleFilePath + ".2")) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }
                Assert.assertEquals(new String(Hex.encodeHex(md5.digest())), getMD5());
            }

            // 0字节文件
            input = ResumableCopyObjectInput.builder().bucket(Consts.bucket).key(key).enableCheckpoint(true).taskNum(3)
                    .checkpointFile(sampleFilePath + ".copy").partSize(partSize).srcBucket(bucket).srcKey(srcEmptyKey).build();
            output = getHandler().resumableCopyObject(input);
            Assert.assertEquals(output.getBucket(), Consts.bucket);
            Assert.assertEquals(output.getKey(), key);

            getHandler().getObjectToFile(GetObjectToFileInput.builder().bucket(Consts.bucket)
                    .key(key).filePath(emptyFilePath + ".copy1").build());
            File file = new File(emptyFilePath + ".copy1");
            Assert.assertEquals(file.length(), 0);
        } catch (TosException | IOException | NoSuchAlgorithmException e) {
            testFailed(e);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput().setBucket(Consts.bucket).setKey(key));
            ClientInstance.getObjectRequestHandlerInstance().deleteObject(new DeleteObjectInput().setBucket(Consts.bucket).setKey(srcKey));
            new File(sampleFilePath + ".1").delete();
            new File(sampleFilePath + ".2").delete();
            new File(emptyFilePath + ".copy1").delete();
        }
    }

    @Test
    void resumableCopyNullObjectTest() {
        String srcKey = Consts.internalFileCrudPrefix + getUniqueObjectKey();
        String key = Consts.internalFileCrudPrefix + getUniqueObjectKey();

        // upload src data
        ClientInstance.getObjectRequestHandlerInstance().putObject(new PutObjectInput()
                .setContent(new ByteArrayInputStream("".getBytes()))
                .setBucket(bucket).setKey(srcKey).setContentLength(0));

        ResumableCopyObjectInput input = ResumableCopyObjectInput.builder()
                .bucket(Consts.bucket).key(key)
                .enableCheckpoint(true)
                .taskNum(3)
                .partSize(5 * 1024 * 1024)
                .srcBucket(bucket)
                .srcKey(srcKey)
                .checkpointFile(sampleFilePath + ".copy")
                .build();

        ResumableCopyObjectOutput output = getHandler().resumableCopyObject(input);
        Assert.assertEquals(output.getBucket(), Consts.bucket);
        Assert.assertEquals(output.getKey(), key);
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

    private void testFailed(Exception e) {
        LOG.error("object test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
