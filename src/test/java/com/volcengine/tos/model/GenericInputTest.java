package com.volcengine.tos.model;

import com.volcengine.tos.*;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.model.bucket.GetBucketACLInput;
import com.volcengine.tos.model.bucket.GetBucketACLOutput;
import com.volcengine.tos.model.object.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

public class GenericInputTest {
    private static TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
            .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());

    @Test
    void HeadersTest(){
        String endpoint = Consts.endpoint;
        String region = Consts.region;
        String accessKey = Consts.accessKey;
        String secretKey = Consts.secretKey;
        String bucketName = Consts.bucket;
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetBucketACLInput input = new GetBucketACLInput().setBucket(bucketName);
            //用户自定义headers query
            input.setRequestHeader("Content-Length","100");
            input.setRequestHeader("content-length","100");
            input.setRequestHeader("Content-Type","application/json");
            input.setRequestQuery("A","a");
            input.setRequestQuery("B","b");
            GetBucketACLOutput output = tos.getBucketACL(input);

            String data = "1234567890abcdeadfghijklmnopqrst2313uvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
            ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey);
            PutObjectInput putObjectInput = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
            basicInput.setRequestHeader("Content-Length","100");
            basicInput.setRequestHeader("content-length","100");
            basicInput.setRequestHeader("Content-Type","application/json");
            basicInput.setRequestQuery("A","a");
            basicInput.setRequestQuery("B","b");
            PutObjectOutput putObjectOutput = tos.putObject(putObjectInput);
            DeleteObjectInput deleteObjectInput = new DeleteObjectInput().setBucket(bucketName).setKey(objectKey);
            DeleteObjectOutput deleteObjectOutput = tos.deleteObject(deleteObjectInput);

        } catch (TosClientException e) {
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            Assert.fail(e.getMessage());
        } catch (TosServerException e) {
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            Assert.fail(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            Assert.fail(t.getMessage());
        }
    }
}
