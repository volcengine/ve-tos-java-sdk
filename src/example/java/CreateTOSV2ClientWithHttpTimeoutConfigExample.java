package example.java;

import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class CreateTOSV2ClientWithHttpTimeoutConfigExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        int readTimeoutMills = 30000;
        int writeTimeoutMills = 30000;
        int connectTimeoutMills = 10000;

        TransportConfig config = TransportConfig.builder()
                .readTimeoutMills(readTimeoutMills)
                .writeTimeoutMills(writeTimeoutMills)
                .connectTimeoutMills(connectTimeoutMills)
                .build();
        TOSClientConfiguration configuration = TOSClientConfiguration.builder()
                .transportConfig(config)
                .region(region)
                .endpoint(endpoint)
                .credentials(new StaticCredentials(accessKey, secretKey))
                .build();

        TOSV2 tos = new TOSV2ClientBuilder().build(configuration);
        // do your operation...
    }
}
