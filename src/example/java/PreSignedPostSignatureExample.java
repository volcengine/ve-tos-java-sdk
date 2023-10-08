package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.model.object.PreSignedPostSignatureInput;
import com.volcengine.tos.model.object.PreSignedPostSignatureOutput;

public class PreSignedPostSignatureExample {
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
            PreSignedPostSignatureInput input = new PreSignedPostSignatureInput().setBucket(bucketName)
                    .setKey(objectKey).setExpires(expires);
            PreSignedPostSignatureOutput output = tos.preSignedPostSignature(input);
            System.out.println("preSignedPostSignature succeed, the signature is " + output.getSignature());
            System.out.println("the credential is " + output.getCredential());
            System.out.println("the policy is " + output.getPolicy());
            System.out.println("the date is " + output.getDate());
            System.out.println("the algorithm is " + output.getAlgorithm());
            System.out.println("the originPolicy is " + output.getOriginPolicy());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("preSignedPostSignature failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("preSignedPostSignature failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
