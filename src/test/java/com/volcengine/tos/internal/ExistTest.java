package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.UUID;


public class ExistTest {

    private static final String endpoint = Consts.endpoint;
    private static final String region = Consts.region;
    private static final String accessKey = Consts.accessKey;
    private static final String secretKey = Consts.secretKey;
    private static final String bucketName = Consts.bucket;
    @Test
    public void doesBucketExistTest(){
        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            // 创建桶
            String bucketName = UUID.randomUUID().toString();
            CreateBucketV2Input createBucketV2Input = new CreateBucketV2Input().setBucket(bucketName);
            CreateBucketV2Output createBucketV2Output = tos.createBucket(createBucketV2Input);
            Assert.assertEquals(createBucketV2Output.getRequestInfo().getStatusCode(), HttpStatus.OK);
            // 查找桶
            DoesBucketExistInput doesBucketExistInput = new DoesBucketExistInput();
            doesBucketExistInput.setBucket(bucketName);
            boolean bucketExists = tos.doesBucketExist(doesBucketExistInput);
            Assert.assertTrue(bucketExists);
            // 删除桶
            DeleteBucketInput deleteBucketInput = new DeleteBucketInput().setBucket(bucketName);
            DeleteBucketOutput deleteBucketOutput = tos.deleteBucket(deleteBucketInput);
            Assert.assertEquals(deleteBucketOutput.getRequestInfo().getStatusCode(), HttpStatus.NO_CONTENT);
            // 再查找桶
            doesBucketExistInput.setBucket(bucketName);
            bucketExists = tos.doesBucketExist(doesBucketExistInput);
            Assert.assertFalse(bucketExists);
        } catch (Throwable t) {
            Assert.fail(t.getMessage());
        }
    }

    @Test
    public void doesObjectExistTest(){
        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
              // 创建桶 和 桶中的对象
            String bucketName = UUID.randomUUID().toString();
            CreateBucketV2Input createBucketV2Input = new CreateBucketV2Input().setBucket(bucketName);
            CreateBucketV2Output createBucketV2Output = tos.createBucket(createBucketV2Input);
            Assert.assertEquals(createBucketV2Output.getRequestInfo().getStatusCode(), HttpStatus.OK);

            String data = "1234757fafa567890abcdefgh53543ijkldsa da dmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
            String objectKey = "example_dir/example_object.txt";
            ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey);
            PutObjectInput putObjectInput = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
            PutObjectOutput putObjectOutput = tos.putObject(putObjectInput);
            Assert.assertEquals(putObjectOutput.getRequestInfo().getStatusCode(), HttpStatus.OK);
              // 判断对象是否存在
            DoesObjectExistInput doesObjectExistInput = new DoesObjectExistInput();
            doesObjectExistInput.setBucket(bucketName);
            doesObjectExistInput.setKey(objectKey);
            boolean objectExists = tos.doesObjectExist(doesObjectExistInput);
            Assert.assertTrue(objectExists);
              // 删除桶中的对象
            DeleteObjectInput deleteObjectInput = new DeleteObjectInput().setBucket(bucketName).setKey(objectKey);
            DeleteObjectOutput deleteObjectOutput = tos.deleteObject(deleteObjectInput);
            Assert.assertEquals(deleteObjectOutput.getRequestInfo().getStatusCode(), HttpStatus.NO_CONTENT);
              // 判断对象是否存在
            objectExists = tos.doesObjectExist(doesObjectExistInput);
            Assert.assertFalse(objectExists);
              // 删除桶
            DeleteBucketInput deleteBucketInput = new DeleteBucketInput().setBucket(bucketName);
            DeleteBucketOutput deleteBucketOutput = tos.deleteBucket(deleteBucketInput);
            Assert.assertEquals(deleteBucketOutput.getRequestInfo().getStatusCode(), HttpStatus.NO_CONTENT);
        } catch (Throwable t) {
            Assert.fail(t.getMessage());
        }
    }



}
