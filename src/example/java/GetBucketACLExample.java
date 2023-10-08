package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.bucket.GetBucketACLInput;
import com.volcengine.tos.model.bucket.GetBucketACLOutput;

public class GetBucketACLExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetBucketACLInput input = new GetBucketACLInput().setBucket(bucketName);
            GetBucketACLOutput output = tos.getBucketACL(input);
            System.out.println("getBucketACL succeed");
            if (output.getOwner() != null) {
                System.out.println("owner id is " + output.getOwner().getId());
                System.out.println("owner display name is " + output.getOwner().getDisplayName());
            }
            if (output.getGrants() != null) {
                System.out.println("this bucket has " + output.getGrants().size() + " grants");
                for (int i = 0; i < output.getGrants().size(); i++){
                    GrantV2 grant = output.getGrants().get(i);
                    if (grant.getGrantee() != null) {
                        System.out.printf("bucket acl grantee, id is %s, displayName is %s, type is %s, uri is %s.\n",
                                grant.getGrantee().getId(), grant.getGrantee().getDisplayName(),
                                grant.getGrantee().getType(), grant.getGrantee().getCanned());
                    }
                    System.out.println("bucket acl grant permission is " + grant.getPermission() + ".");
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getBucketACL failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getBucketACL failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getBucketACL failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
