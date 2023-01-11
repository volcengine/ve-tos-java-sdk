package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.auth.Credential;
import com.volcengine.tos.auth.FederationCredentials;
import com.volcengine.tos.auth.FederationToken;
import com.volcengine.tos.auth.FederationTokenProvider;

import java.time.LocalDateTime;

public class FederationTokenRefreshExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        // 以下代码仅为示例
        MockFederationTokenProvider tokenProvider = new MockFederationTokenProvider().setProvider(
                // 自定义签发 Token 的逻辑
                () -> new FederationToken().setCredential(new Credential(accessKey, secretKey, ""))
                        .setExpiration(LocalDateTime.now().plusMinutes(10)));
        FederationCredentials tokenCredential = new FederationCredentials(tokenProvider);
        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, tokenCredential);
        // do your operation...
    }
}

// 签发 Token 的接口
@FunctionalInterface
interface Provider{
    FederationToken provider();
}

// 自定义实现 FederationTokenProvider 接口，在 federationToken() 中签发 Token
class MockFederationTokenProvider implements FederationTokenProvider {
    // 仅为示例，可自行实现签发 token 的逻辑
    private Provider provider;
    @Override
    public FederationToken federationToken(){
        return provider.provider();
    }

    public MockFederationTokenProvider setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }
}