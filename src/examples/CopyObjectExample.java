package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.CopyObjectV2Input;
import com.volcengine.tos.model.object.CopyObjectV2Output;

public class CopyObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String srcBucketName = "your src bucket name";
        String srcObjectKey = "your src object key";
        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CopyObjectV2Input input = new CopyObjectV2Input().setBucket(bucketName).setKey(objectKey)
                    .setSrcBucket(srcBucketName).setSrcKey(srcObjectKey);
            CopyObjectV2Output output = tos.copyObject(input);
            System.out.println("copyObject succeed, object's etag is " + output.getEtag());
            System.out.println("copyObject succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("copyObject failed");
            e.printStackTrace();
        }
    }
}
