package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;

public class PreSignedUrlGetObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";
        // 单位为秒，设置3600秒即1小时后过期
        long expires = 3600;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucketName).setKey(objectKey)
                    .setHttpMethod(HttpMethod.GET).setExpires(expires);
            PreSignedURLOutput output = tos.preSignedURL(input);
            System.out.println("preSignedURL succeed, the signed url is " + output.getSignedUrl());
            System.out.println("preSignedURL succeed, the signed header is " + output.getSignedHeader());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("preSignedURL failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("preSignedURL failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
