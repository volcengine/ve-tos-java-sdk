package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.AccessLogConfiguration;
import com.volcengine.tos.model.bucket.PutBucketRealTimeLogInput;
import com.volcengine.tos.model.bucket.PutBucketRealTimeLogOutput;
import com.volcengine.tos.model.bucket.RealTimeLogConfiguration;

public class PutBucketRealTimeLogExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        String realTimeLogRole = "your real time log role";
        String tlsProjectId = "the tls project id";
        String tlsTopicId = "the tls topic id";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            RealTimeLogConfiguration configuration = new RealTimeLogConfiguration().setRole(realTimeLogRole)
                    .setConfiguration(new AccessLogConfiguration()
                            .setUseServiceTopic(true)
                            .setTlsProjectID(tlsProjectId)
                            .setTlsTopicID(tlsTopicId));
            PutBucketRealTimeLogInput input = new PutBucketRealTimeLogInput().setBucket(bucketName)
                    .setConfiguration(configuration);
            PutBucketRealTimeLogOutput output = tos.putBucketRealTimeLog(input);
            System.out.println("putBucketRealTimeLog succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketRealTimeLog failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketRealTimeLog failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketRealTimeLog failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
