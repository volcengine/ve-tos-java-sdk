package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.DeleteBucketCustomDomainInput;
import com.volcengine.tos.model.bucket.DeleteBucketCustomDomainOutput;

public class DeleteBucketCustomDomainExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucketName = "bucket-example";

        String customDomain = "example.test.com";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            DeleteBucketCustomDomainInput input = new DeleteBucketCustomDomainInput().setBucket(bucketName)
                    .setDomain(customDomain);
            DeleteBucketCustomDomainOutput output = tos.deleteBucketCustomDomain(input);
            System.out.println("deleteBucketCustomDomain succeed, " + output);
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("deleteBucketCustomDomain failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("deleteBucketCustomDomain failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("deleteBucketCustomDomain failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
