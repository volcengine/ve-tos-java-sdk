package example.java;

import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class CreateTOSV2ClientWithConfigExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        boolean enableCrc = true;
        boolean clientAutoRecognizeContentType = true;
        boolean enableVerifySSL = true;
        int readTimeoutMills = 30000;
        int writeTimeoutMills = 30000;
        int connectTimeoutMills = 10000;
        int idleConnectionTimeMills = 10000;
        int maxConnections = 1024;
        int maxRetryCount = 3;
        int dnsCacheTimeMills = 0;

        String proxyHost = "your proxy host";
        int proxyPort = 8080;
        String proxyUserName = "your proxy user name";
        String proxyPassword = "your proxy password";

        TransportConfig config = TransportConfig.builder()
                .enableVerifySSL(enableVerifySSL)
                .readTimeoutMills(readTimeoutMills)
                .writeTimeoutMills(writeTimeoutMills)
                .connectTimeoutMills(connectTimeoutMills)
                .idleConnectionTimeMills(idleConnectionTimeMills)
                .maxConnections(maxConnections)
                .maxRetryCount(maxRetryCount)
                .dnsCacheTimeMills(dnsCacheTimeMills)
                .build();
        TOSClientConfiguration configuration = TOSClientConfiguration.builder()
                .transportConfig(config)
                .region(region)
                .endpoint(endpoint)
                .clientAutoRecognizeContentType(clientAutoRecognizeContentType)
                .enableCrc(enableCrc)
                .credentials(new StaticCredentials(accessKey, secretKey))
                .build();

        TOSV2 tos = new TOSV2ClientBuilder().build(configuration);
        // do your operation...
    }
}
