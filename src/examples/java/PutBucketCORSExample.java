package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.*;

import java.util.ArrayList;
import java.util.List;

public class PutBucketCORSExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            List<CORSRule> rules = new ArrayList<>();
            List<String> allowedOrigins = new ArrayList<>();
            allowedOrigins.add("*");
            List<String> allowedMethods = new ArrayList<>();
            allowedMethods.add("GET");
            allowedMethods.add("DELETE");
            allowedMethods.add("PUT");
            List<String> allowedHeaders = new ArrayList<>();
            allowedHeaders.add("Authorization");
            List<String> exposeHeaders = new ArrayList<>();
            exposeHeaders.add("X-TOS-HEADER-1");
            exposeHeaders.add("X-TOS-HEADER-2");
            CORSRule rule1 = new CORSRule()
                    .setAllowedOrigins(allowedOrigins)
                    .setAllowedMethods(allowedMethods)
                    .setAllowedHeaders(allowedHeaders)
                    .setExposeHeaders(exposeHeaders)
                    .setMaxAgeSeconds(3600);
            rules.add(rule1);
            PutBucketCORSInput input = new PutBucketCORSInput().setBucket(bucketName).setRules(rules);
            PutBucketCORSOutput output = tos.putBucketCORS(input);
            System.out.println("putBucketCORS succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketCORS failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketCORS failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketCORS failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
