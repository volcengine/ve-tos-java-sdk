package example.java;

import com.volcengine.tos.ClientOptions;
import com.volcengine.tos.TOSClient;
import com.volcengine.tos.auth.StaticCredentials;

public class CreateTOSV1ClientExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        TOSClient client = new TOSClient(endpoint, ClientOptions.withRegion(region),
                ClientOptions.withCredentials(new StaticCredentials(accessKey, secretKey)));
        // do your operation...
    }
}
