package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.comm.common.StorageClassInheritDirectiveType;
import com.volcengine.tos.model.bucket.Destination;
import com.volcengine.tos.model.bucket.PutBucketReplicationInput;
import com.volcengine.tos.model.bucket.PutBucketReplicationOutput;
import com.volcengine.tos.model.bucket.ReplicationRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PutBucketReplicationExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        // 目标region
        String destinationRegion = "your destination region";
        // 目标bucket
        String destinationBucket = "your destination bucket";
        String role = "your role";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            ReplicationRule rule = new ReplicationRule()
                    // 设置规则id，必选
                    .setId("1")
                    // 指定是否开启跨区域复制规则
                    .setStatus(StatusType.STATUS_ENABLED)
                    // 指定跨区域复制匹配对象的前缀，不设置则默认复制全部对象
                    .setPrefixSet(Arrays.asList("prefix1", "prefix2"))
                    // 指定复制到的目标信息
                    .setDestination(new Destination()
                            // 目的桶和目的region，必选
                            .setBucket(destinationBucket).setLocation(destinationRegion)
                            // 指定复制对象的存储类型规则
                            // STORAGE_CLASS_ID_SOURCE_OBJECT: 与源对象保持一致
                            // STORAGE_CLASS_ID_DESTINATION_BUCKET: 与目标桶保持一致
                            .setStorageClassInheritDirectiveType(StorageClassInheritDirectiveType.STORAGE_CLASS_ID_SOURCE_OBJECT))
                    // 是否需要复制历史对象
                    .setHistoricalObjectReplication(StatusType.STATUS_DISABLED);
            List<ReplicationRule> rules = new ArrayList<>();
            rules.add(rule);
            PutBucketReplicationInput input = new PutBucketReplicationInput().setBucket(bucketName).setRules(rules).setRole(role);
            PutBucketReplicationOutput output = tos.putBucketReplication(input);
            System.out.println("putBucketReplication succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketReplication failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketReplication failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketReplication failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
