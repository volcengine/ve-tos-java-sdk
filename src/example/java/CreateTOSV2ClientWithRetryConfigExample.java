package example.java;

import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class CreateTOSV2ClientWithRetryConfigExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        // 设置最大重试次数为 3 次，可根据实际需要调整
        int maxRetryCount = 3;

        TransportConfig config = TransportConfig.builder()
                .maxRetryCount(maxRetryCount)
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
