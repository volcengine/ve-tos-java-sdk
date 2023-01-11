package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.comm.HttpMethod;

import java.time.Duration;

public class PreSignedUrlGetObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        String signed = tos.preSignedURL(HttpMethod.GET, bucketName, objectKey, Duration.ofHours(1));
        System.out.println("generated url is " + signed);
    }
}
