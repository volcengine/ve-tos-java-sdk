package com.volcengine.tos.internal;

import com.volcengine.tos.TosException;
import com.volcengine.tos.Consts;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.bucket.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class TosBucketRequestHandlerTest {

    private TosBucketRequestHandler getHandler() {
        return ClientInstance.getBucketRequestHandlerInstance();
    }

    @Test
    void bucketCreateTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            CreateBucketV2Output output = getHandler().createBucket(input);
            Assert.assertEquals(output.getLocation(), "/"+bucketName);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        }
    }

    @Test
    void bucketCreateWithAllParamsTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder()
                    .bucket(bucketName)
                    .acl(ACLType.ACL_BUCKET_OWNER_READ)
                    .storageClass(StorageClassType.STORAGE_CLASS_IA)
                    .build();
            CreateBucketV2Output output = getHandler().createBucket(input);
            Assert.assertEquals(output.getLocation(), "/"+bucketName);
            Thread.sleep(5 * 1000);
            HeadBucketV2Input headInput = HeadBucketV2Input.builder().bucket(bucketName).build();
            HeadBucketV2Output headOutput = getHandler().headBucket(headInput);
            Assert.assertEquals(headOutput.getRegion(), Consts.region);
            // todo 这里目前只能校验 storage class，等 getBucketACL 接口完成后校验 acl
            Assert.assertEquals(headOutput.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        }
    }

    @Test
    void bucketHeadTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input createInput = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(createInput);
            Thread.sleep(5 * 1000);
            HeadBucketV2Input headInput = HeadBucketV2Input.builder().bucket(bucketName).build();
            HeadBucketV2Output headOutput = getHandler().headBucket(headInput);
            Assert.assertEquals(headOutput.getRegion(), Consts.region);
            Assert.assertEquals(headOutput.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        }
    }

    @Test
    void bucketDeleteTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(input);
            Thread.sleep(5 * 1000);
            DeleteBucketOutput output = getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
            Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            testFailed(e);
        }
        try{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }
    }

    @Test
    void bucketListTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try {
            ListBucketsV2Output output = getHandler().listBuckets(new ListBucketsV2Input());
            int bucketsNum = output.getBuckets().size();
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(input);
            Thread.sleep(5 * 1000);
            output = getHandler().listBuckets(new ListBucketsV2Input());
            Assert.assertEquals(output.getBuckets().size(), bucketsNum + 1);
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
            output = getHandler().listBuckets(new ListBucketsV2Input());
            Assert.assertEquals(output.getBuckets().size(), bucketsNum);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void bucketNameValidateTest() {
        String longLengthBucketName = RandomStringUtils.randomAlphanumeric(64);
        List<String> bucketNameInvalidList = Arrays.asList(null, "", "1", longLengthBucketName);
        for (String name :bucketNameInvalidList) {
            try{
                CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(name).build();
                getHandler().createBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid bucket name, the length must be [3, 63]");
            }
        }
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket("-a-").build();
            getHandler().createBucket(input);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "invalid bucket name, the bucket name can be neither starting with '-' nor ending with '-'");
        }
        bucketNameInvalidList = Arrays.asList("&*(%^&", "ABCD", "中文测试");
        for (String name : bucketNameInvalidList) {
            try{
                CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(name).build();
                getHandler().createBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid bucket name, the character set is illegal");
            }
        }
    }

    private void testFailed(Exception e) {
        Consts.LOG.error("bucket test failed, {}", e.toString());
        Assert.fail();
    }
}
