package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.bucket.*;

public class ListBucketExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            ListBucketsV2Input input = new ListBucketsV2Input();
            ListBucketsV2Output output = tos.listBuckets(input);
            System.out.println("bucket owner is " + output.getOwner());
            if (output.getBuckets() != null) {
                System.out.println("you have listed " + output.getBuckets().size() + " buckets");
                for (int i = 0; i < output.getBuckets().size(); i++){
                    ListedBucket bucket = output.getBuckets().get(i);
                    System.out.printf("the No.%d bucket is %s\n", i, bucket.toString());
                }
            } else {
                System.out.println("there are no buckets in your account.");
            }
        } catch (TosException e) {
            System.out.println("listBuckets failed");
            e.printStackTrace();
        }
    }
}
