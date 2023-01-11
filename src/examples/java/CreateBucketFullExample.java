package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.CreateBucketV2Output;

public class CreateBucketFullExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        // 设置桶名，请保证桶名的唯一性
        String bucketName = "bucket-example";

        // 创建桶是可以设置桶的访问权限和存储类型等
        // 注意以下代码仅为示例，不是必选参数
        // 设置桶的访问权限
        ACLType aclType = ACLType.ACL_PRIVATE;
        // 设置桶的存储类型
        StorageClassType storageClassType = StorageClassType.STORAGE_CLASS_STANDARD;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CreateBucketV2Input input = new CreateBucketV2Input().setBucket(bucketName)
                    .setAcl(aclType).setStorageClass(storageClassType);
            CreateBucketV2Output output = tos.createBucket(input);
            System.out.println("bucket created: " + output.getLocation());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("createBucket failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("createBucket failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("createBucket failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
