package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.bucket.DeleteBucketInput;
import com.volcengine.tos.model.bucket.DeleteBucketOutput;

public class DeleteBucketExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucketName = "your bucket name";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            DeleteBucketInput input = new DeleteBucketInput().setBucket(bucketName);
            DeleteBucketOutput output = tos.deleteBucket(input);
            System.out.println("deleteBucket succeed, " + output);
        } catch (TosException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("deleteBucket failed, the bucket is not found.");
            } else {
                System.out.println("deleteBucket failed.");
            }
            e.printStackTrace();
        }
    }
}
