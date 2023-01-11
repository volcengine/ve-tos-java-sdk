package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;

public class CreateTOSV2ClientWithSTSExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String securityToken = "your security token";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey, securityToken);
        // do your operation...
    }
}
