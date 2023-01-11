package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.HeadObjectV2Input;
import com.volcengine.tos.model.object.HeadObjectV2Output;

public class HeadObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            HeadObjectV2Input input = new HeadObjectV2Input().setBucket(bucketName).setKey(objectKey);
            HeadObjectV2Output output = tos.headObject(input);
            System.out.println("headObject succeed, object's metadata is " + output.getHeadObjectBasicOutput());
        } catch (TosException e) {
            if (e.getStatusCode() == 404) {
                // 下载不存在的对象会返回404
                System.out.println("the object is not found");
            } else {
                System.out.println("headObject failed");
            }
            e.printStackTrace();
        }
    }
}
