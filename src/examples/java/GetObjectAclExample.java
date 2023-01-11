package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.object.GetObjectACLV2Input;
import com.volcengine.tos.model.object.GetObjectACLV2Output;

public class GetObjectAclExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetObjectACLV2Input input = new GetObjectACLV2Input().setBucket(bucketName).setKey(objectKey);
            GetObjectACLV2Output output = tos.getObjectAcl(input);
            System.out.println("getObjectAcl succeed.");
            System.out.println("object owner is " + output.getOwner());
            if (output.getGrants() != null && output.getGrants().size() > 0) {
                for (GrantV2 grant : output.getGrants()) {
                    System.out.println("object grant is " + grant);
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getObjectAcl failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getObjectAcl failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getObjectAcl failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
