package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;

public class HeadBucketExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucketName = "your bucket name";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            HeadBucketV2Input input = new HeadBucketV2Input().setBucket(bucketName);
            HeadBucketV2Output output = tos.headBucket(input);
            System.out.println("bucket's region is " + output.getRegion());
            System.out.println("bucket's storage class is " + output.getStorageClass());
        } catch (TosException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("headBucket failed, the bucket is not found.");
            } else {
                System.out.println("headBucket failed.");
            }
            e.printStackTrace();
        }
    }
}
