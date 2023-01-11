package example.java;

import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.transport.TransportConfig;

public class CreateTOSV2ClientWithProxyExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        // 代理服务器地址
        String proxyHost = "your proxy host";
        // 代理服务器端口，请填写您的代理服务器端口，此处仅为示例
        int proxyPort = 8080;
        // 代理服务器用户名
        String proxyUserName = "your proxy user name";
        // 代理服务器密码
        String proxyPassword = "your proxy password";

        TransportConfig config = TransportConfig.builder()
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUserName(proxyUserName)
                .proxyPassword(proxyPassword)
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
